package com.techlab.api_techlab.dto;

// Una línea del pedido tal como la envía el cliente: qué producto y cuántas unidades.
public class ItemPedidoRequest {

    private int productoId;
    private int cantidad;

    // Constructor vacío y setters: los necesita Jackson para convertir el JSON
    public ItemPedidoRequest() {
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}