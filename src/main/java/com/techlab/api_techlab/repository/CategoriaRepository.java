package com.techlab.api_techlab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.api_techlab.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}