package com.techlab.api_techlab.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.api_techlab.model.Categoria;
import com.techlab.api_techlab.service.CategoriaService;

@RestController
@RequestMapping("/categorias")

public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // GET /categorias -> devuelve la lista de categorías ([{id, nombre}, ...])
    // Si no hay categorías devuelve [] con código 200.
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listarTodas()); // 200
    }
}