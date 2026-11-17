package com.techlab.api_techlab.dto;

import java.math.BigDecimal;

// Una línea del pedido tal como la devuelve la API.
public class LineaPedidoResponse {

    private int productoId;
    private String producto;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public LineaPedidoResponse(int productoId, String producto, int cantidad,
            BigDecimal precioUnitario, BigDecimal subtotal) {
        this.productoId = productoId;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public int getProductoId() {
        return productoId;
    }

    public String getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}