package com.techlab.api_techlab.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.techlab.api_techlab.dto.ItemPedidoRequest;
import com.techlab.api_techlab.dto.LineaPedidoResponse;
import com.techlab.api_techlab.dto.PedidoRequest;
import com.techlab.api_techlab.dto.PedidoResponse;
import com.techlab.api_techlab.exception.PedidoNoEncontradoException;
import com.techlab.api_techlab.exception.StockInsuficienteException;
import com.techlab.api_techlab.model.LineaPedido;
import com.techlab.api_techlab.model.Pedido;
import com.techlab.api_techlab.model.Producto;
import com.techlab.api_techlab.repository.LineaPedidoRepository;
import com.techlab.api_techlab.repository.PedidoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final LineaPedidoRepository lineaPedidoRepository;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository,
            LineaPedidoRepository lineaPedidoRepository,
            ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.lineaPedidoRepository = lineaPedidoRepository;
        this.productoService = productoService;
    }

    // POST /pedidos
    // Se trabaja en dos pasos para no dejar un pedido a medias:
    // 1) se validan TODAS las líneas (sin tocar la base);
    // 2) recién cuando todo es válido se guarda el pedido y se descuenta el stock.
    // Si una línea falla en el paso 1, no se modificó nada.
    public PedidoResponse crear(PedidoRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un producto.");
        }

        // ----- Paso 1: validar -----
        List<Producto> productos = new ArrayList<>();
        List<Integer> idsVistos = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequest item : request.getItems()) {
            if (item.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
            }
            if (idsVistos.contains(item.getProductoId())) {
                throw new IllegalArgumentException(
                        "El producto " + item.getProductoId() + " está repetido en el pedido.");
            }
            idsVistos.add(item.getProductoId());

            // Si el producto no existe, obtenerPorId lanza la excepción (404)
            Producto p = productoService.obtenerPorId(item.getProductoId());

            if (p.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para " + p.getNombre()
                        + ": disponible " + p.getStock() + ", pedido " + item.getCantidad() + ".");
            }

            productos.add(p);
            // precio x cantidad, con BigDecimal
            total = total.add(p.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        // ----- Paso 2: guardar -----
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);

        List<LineaPedido> lineas = new ArrayList<>();
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            int cantidad = request.getItems().get(i).getCantidad();

            LineaPedido linea = new LineaPedido();
            linea.setPedido(pedido);
            linea.setProducto(p);
            linea.setCantidad(cantidad);
            linea.setPrecioUnitario(p.getPrecio());
            lineas.add(lineaPedidoRepository.save(linea));

            productoService.descontarStock(p, cantidad);
        }

        return armarRespuesta(pedido, lineas);
    }

    // GET /pedidos
    public List<PedidoResponse> listarTodos() {
        List<PedidoResponse> respuesta = new ArrayList<>();
        for (Pedido pedido : pedidoRepository.findAll()) {
            List<LineaPedido> lineas = lineaPedidoRepository.findByPedidoId(pedido.getId());
            respuesta.add(armarRespuesta(pedido, lineas));
        }
        return respuesta;
    }

    // GET /pedidos/{id}
    public PedidoResponse obtenerPorId(int id) {
        Optional<Pedido> resultado = pedidoRepository.findById(id);
        if (resultado.isEmpty()) {
            throw new PedidoNoEncontradoException("No se encontro un pedido con el id " + id);
        }
        Pedido pedido = resultado.get();
        return armarRespuesta(pedido, lineaPedidoRepository.findByPedidoId(id));
    }

    // Convierte el pedido y sus líneas (entidades) en el DTO que se devuelve
    private PedidoResponse armarRespuesta(Pedido pedido, List<LineaPedido> lineas) {
        List<LineaPedidoResponse> detalle = new ArrayList<>();
        for (LineaPedido l : lineas) {
            BigDecimal subtotal = l.getPrecioUnitario().multiply(BigDecimal.valueOf(l.getCantidad()));
            detalle.add(new LineaPedidoResponse(
                    l.getProducto().getId(),
                    l.getProducto().getNombre(),
                    l.getCantidad(),
                    l.getPrecioUnitario(),
                    subtotal));
        }
        return new PedidoResponse(pedido.getId(), pedido.getFecha(), pedido.getTotal(), detalle);
    }
}