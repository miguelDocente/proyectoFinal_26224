package com.techlab.api_techlab.repository;

import com.techlab.api_techlab.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}