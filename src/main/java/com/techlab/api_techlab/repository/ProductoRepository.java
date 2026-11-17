package com.techlab.api_techlab.repository;

import com.techlab.api_techlab.dto.ProductoConCategoria;
import com.techlab.api_techlab.model.Producto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techlab.api_techlab.dto.ProductoConCategoria;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByNombreContainingIgnoreCase(String texto);

    List<Producto> findByCategoriaNombre(String nombre);

    @Query("SELECT SUM(p.precio * p.stock) FROM Producto p")
    BigDecimal calcularValorInventario();

    @Query("SELECT new com.techlab.api_techlab.dto.ProductoConCategoria(p.id, p.nombre, p.precio, p.stock, c.nombre) "
            + "FROM Producto p JOIN p.categoria c")
    List<ProductoConCategoria> listarConCategoria();

    // ¿Ya existe un producto con ese nombre? (sin distinguir mayúsculas)
    boolean existsByNombreIgnoreCase(String nombre);

    // Igual, pero ignorando al producto con ese id: sirve al editar,
    // para que un producto pueda conservar su propio nombre.
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, int id);
}