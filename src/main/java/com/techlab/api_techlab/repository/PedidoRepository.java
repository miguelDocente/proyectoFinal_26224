package com.techlab.api_techlab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.api_techlab.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}