package com.techlab.api_techlab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejo centralizado de errores.
 *
 * PROBLEMA QUE RESUELVE
 * El servicio lanza excepciones (producto no encontrado, datos inválidos, etc.)
 * y el controlador no las captura. Cuando una excepción llega sin atender hasta
 * Spring, este responde 500 (Internal Server Error), que significa "falló el
 * servidor". Pero la mayoría de estos casos son errores del CLIENTE (pidió un
 * producto que no existe, envió un precio negativo), y para eso corresponden
 * códigos 4xx.
 *
 * COMO LO RESUELVE
 * - @RestControllerAdvice marca esta clase como "asesora" de TODOS los
 *   controladores: intercepta las excepciones que estos no capturaron.
 * - @ExceptionHandler(X.class) marca el método que se ejecuta cuando se lanza
 *   la excepción X. Recibe esa excepción como parámetro, por lo que puede usar
 *   e.getMessage() para devolver el mensaje que escribió el servicio.
 *
 * VENTAJA
 * Los controladores quedan sin try/catch. El criterio "qué excepción produce
 * qué código HTTP" se define en un único lugar.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ProductoNoEncontradoException (lanzada por obtenerPorId, actualizar y eliminar)
    // -> 404 Not Found: el recurso que se pidió no existe.
    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<String> manejarNoEncontrado(ProductoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());     // 404
    }

    // StockInsuficienteException (lanzada por Validador.validarStock)
    // -> 400 Bad Request: el cliente envió un dato inválido.
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<String> manejarStock(StockInsuficienteException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());   // 400
    }

    // IllegalArgumentException (lanzada por Validador.validarNombre, validarPrecio
    // y validarCategoria). Sin este método, un precio negativo o un nombre vacío
    // también terminarían en 500.
    // -> 400 Bad Request: el cliente envió un dato inválido.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> manejarArgumentoInvalido(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());   // 400
    }

    // [CLASE - PASO 3] ProductoDuplicadoException (lanzada por ProductoService.guardar)
    // -> 409 Conflict: la petición está bien formada y los datos son válidos,
    // pero chocan con lo que ya existe. Por eso no es un 400.
    // Para sumar un error nuevo solo hizo falta esta excepción y este método:
    // el controlador no se modificó.
    @ExceptionHandler(ProductoDuplicadoException.class)
    public ResponseEntity<String> manejarDuplicado(ProductoDuplicadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());      // 409
    }

}
