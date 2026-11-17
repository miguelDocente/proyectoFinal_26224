package com.techlab.api_techlab.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Pedido completo tal como lo devuelve la API: datos del pedido y sus líneas.
public class PedidoResponse {

    private int id;
    private LocalDateTime fecha;
    private BigDecimal total;
    private List<LineaPedidoResponse> lineas;

    public PedidoResponse(int id, LocalDateTime fecha, BigDecimal total, List<LineaPedidoResponse> lineas) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.lineas = lineas;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<LineaPedidoResponse> getLineas() {
        return lineas;
    }
}