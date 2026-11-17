package com.techlab.api_techlab.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.api_techlab.dto.PedidoRequest;
import com.techlab.api_techlab.dto.PedidoResponse;
import com.techlab.api_techlab.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // POST /pedidos -> crea un pedido. Responde 201 (Created) con el pedido completo.
    // Los errores (producto inexistente, stock insuficiente, datos inválidos)
    // los atiende GlobalExceptionHandler.
    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@RequestBody PedidoRequest request) {
        PedidoResponse nuevo = pedidoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo); // 201
    }

    // GET /pedidos -> lista de pedidos. Sin pedidos devuelve [] con código 200.
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listar() {
        return ResponseEntity.ok(pedidoService.listarTodos()); // 200
    }

    // GET /pedidos/{id} -> un pedido. Si no existe, 404 (GlobalExceptionHandler).
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtener(@PathVariable int id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id)); // 200
    }
}