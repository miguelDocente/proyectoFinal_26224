package com.techlab.api_techlab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.api_techlab.model.LineaPedido;

public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Integer> {

    // Líneas de un pedido: recorre la relación LineaPedido -> pedido -> id
    List<LineaPedido> findByPedidoId(int pedidoId);

    // ¿Algún pedido incluye este producto?
    boolean existsByProductoId(int productoId);
}