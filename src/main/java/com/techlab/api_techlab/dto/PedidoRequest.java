package com.techlab.api_techlab.dto;

import java.util.List;

// Cuerpo del POST /pedidos: { "items": [ { "productoId": 1, "cantidad": 2 }, ... ] }
public class PedidoRequest {

    private List<ItemPedidoRequest> items;

    public PedidoRequest() {
    }

    public List<ItemPedidoRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoRequest> items) {
        this.items = items;
    }
}