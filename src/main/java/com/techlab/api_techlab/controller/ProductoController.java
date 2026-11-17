package com.techlab.api_techlab.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.api_techlab.dto.ProductoConCategoria;
import com.techlab.api_techlab.model.Producto;
import com.techlab.api_techlab.service.ProductoService;

import com.techlab.api_techlab.dto.ProductoConCategoria;

@RestController
@RequestMapping("/productos") // prefijo común para todas las rutas de este controlador
//@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ProductoController {

    private final ProductoService productoService;

    // Inyección por constructor. Con un único constructor, @Autowired es
    // opcional (el PDF lo muestra con @Autowired; ambas versiones funcionan).
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // ---------------------------------------------------------------------
    // RESPONSEENTITY
    // Hasta ahora cada método devolvía solo el objeto (Producto o List) y
    // Spring respondía siempre con el código 200. ResponseEntity<T> representa
    // la respuesta HTTP completa: código de estado + cuerpo (+ encabezados).
    // El tipo entre < > es el tipo del CUERPO de la respuesta.
    //
    // ResponseEntity.ok(objeto) -> 200 OK
    // ResponseEntity.status(HttpStatus.CREATED).body(x) -> 201 Created
    // ResponseEntity.noContent().build() -> 204 No Content
    //
    // .body(...) se usa cuando hay datos para devolver.
    // .build() se usa cuando la respuesta no tiene cuerpo.
    // ---------------------------------------------------------------------

    // GET /productos -> devuelve la lista de productos
    // Si no hay productos devuelve [] con código 200 (una lista vacía no es un
    // error).
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listarTodos()); // 200
    }

    // [CLASE - PASO 4] GET /productos/categoria/{categoria} -> productos de esa
    // categoría.
    // @PathVariable toma el valor de la URL, igual que con {id}, pero acá es un
    // String.
    // No choca con "/{id}": esta ruta tiene dos segmentos y la otra uno.
    // No hace falta tocar GlobalExceptionHandler: este endpoint no lanza
    // excepciones.
    // Sin productos en esa categoría responde 200 con [] (igual que GET /productos
    // vacío).
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoria)); // 200
    }

    // GET /productos/{id} -> devuelve un producto
    // @PathVariable toma el valor {id} de la URL y lo pasa como argumento.
    // Si el id no existe, el servicio lanza ProductoNoEncontradoException.
    // Este método no la captura: la atiende GlobalExceptionHandler (404).
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable int id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id)); // 200
    }

    // POST /productos -> @RequestBody convierte el JSON del cuerpo en un Producto.
    // Se responde 201 (Created) porque se creó un recurso nuevo.
    // Antes devolvía 200, que indica éxito pero no que algo se haya creado.
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto p) {
        Producto nuevo = productoService.guardar(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo); // 201
    }

    // PUT /productos/{id} -> actualiza un producto existente.
    // Combina las dos anotaciones:
    // @PathVariable -> indica QUÉ producto se modifica (viene de la URL)
    // @RequestBody -> trae los DATOS NUEVOS (vienen en el cuerpo, como JSON)
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable int id, @RequestBody Producto datos) {
        return ResponseEntity.ok(productoService.actualizar(id, datos)); // 200
    }

    // DELETE /productos/{id} -> elimina un producto.
    // ResponseEntity<Void>: la respuesta no tiene cuerpo, porque el producto ya no
    // existe.
    // El código correcto es 204 (No Content): la operación salió bien y no hay nada
    // que devolver.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204
    }

    // GET /productos?nombre=cafe
    @GetMapping(params = "nombre")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    // GET /productos/valor-inventario
    @GetMapping("/valor-inventario")
    public ResponseEntity<BigDecimal> valorInventario() {
        return ResponseEntity.ok(productoService.calcularValorInventario());
    }

    // GET /productos/con-categoria
    @GetMapping("/con-categoria")
    public ResponseEntity<List<ProductoConCategoria>> listarConCategoria() {
        return ResponseEntity.ok(productoService.listarConCategoria());
    }

}
