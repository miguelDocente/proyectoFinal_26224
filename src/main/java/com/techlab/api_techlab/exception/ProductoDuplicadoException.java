package com.techlab.api_techlab.exception;

// Excepción propia: se lanza cuando se intenta guardar un producto cuyo
// nombre ya existe. Tiene la misma estructura que las otras dos excepciones
// del proyecto: extiende RuntimeException y recibe el mensaje por constructor.
public class ProductoDuplicadoException extends RuntimeException {

    public ProductoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
