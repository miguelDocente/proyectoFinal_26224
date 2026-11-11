package com.techlab.api_techlab.dto;

import java.math.BigDecimal;

public class ProductoConCategoria {

    private int id;
    private String nombre;
    private BigDecimal precio;
    private int stock;
    private String categoria;

    public ProductoConCategoria(int id, String nombre, BigDecimal precio, int stock, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public String getCategoria() {
        return categoria;
    }
}