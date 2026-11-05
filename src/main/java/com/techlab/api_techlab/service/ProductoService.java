package com.techlab.api_techlab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.techlab.api_techlab.exception.ProductoNoEncontradoException;
import com.techlab.api_techlab.model.Categoria;
import com.techlab.api_techlab.model.Producto;
import com.techlab.api_techlab.repository.CategoriaRepository;
import com.techlab.api_techlab.repository.ProductoRepository;
import com.techlab.api_techlab.util.Validador;

@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    // GET /productos
    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    // GET /productos/{id}
    public Producto obtenerPorId(int id) {
        Optional<Producto> resultado = repository.findById(id);
        if (resultado.isEmpty()) {
            throw new ProductoNoEncontradoException("No se encontro un producto con el id " + id);
        }
        return resultado.get();
    }

    // POST /productos
    // La regla de nombre repetido se vuelve a agregar en el paso 18.
    public Producto guardar(Producto p) {
        Validador.validarNombre(p.getNombre());
        Validador.validarPrecio(p.getPrecio());
        Validador.validarStock(p.getStock());

        // El id lo asigna la base: se ignora el que envíe el cliente.
        // Con id 0, save() hace un INSERT.
        p.setId(0);
        p.setCategoria(obtenerCategoria(p));
        return repository.save(p);
    }

    // PUT /productos/{id}
    public Producto actualizar(int id, Producto datos) {
        // Si el id no existe, obtenerPorId lanza la excepción y no se modifica nada
        Producto p = obtenerPorId(id);

        Validador.validarNombre(datos.getNombre());
        Validador.validarPrecio(datos.getPrecio());
        Validador.validarStock(datos.getStock());

        p.setNombre(datos.getNombre());
        p.setPrecio(datos.getPrecio());
        p.setStock(datos.getStock());
        p.setCategoria(obtenerCategoria(datos));

        // Como p ya tiene un id existente, save() hace un UPDATE
        return repository.save(p);
    }

    // DELETE /productos/{id}
    public void eliminar(int id) {
        // Primero se busca, así un id inexistente responde 404
        Producto p = obtenerPorId(id);
        repository.delete(p);
    }

    // GET /productos/categoria/{categoria}
    // Versión temporal: recorre los productos de la base.
    // En el paso 16 se reemplaza por una consulta que filtra en la base.
    public List<Producto> listarPorCategoria(String categoria) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : repository.findAll()) {
            if (p.getCategoria().getNombre().equalsIgnoreCase(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // Comprueba que la categoría enviada exista en la base y la devuelve completa
    private Categoria obtenerCategoria(Producto p) {
        if (p.getCategoria() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria.");
        }
        int idCategoria = p.getCategoria().getId();
        Optional<Categoria> resultado = categoriaRepository.findById(idCategoria);
        if (resultado.isEmpty()) {
            throw new IllegalArgumentException("No existe la categoría con id " + idCategoria);
        }
        return resultado.get();
    }
}