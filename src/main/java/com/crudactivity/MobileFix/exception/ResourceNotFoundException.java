package com.crudactivity.MobileFix.exception;

/**
 * Excepción personalizada para cuando NO se encuentra un recurso en la BD
 * Ejemplo: Usuario no encontrado, Device no encontrado, etc.
 *
 * Extiende de RuntimeException (unchecked exception)
 * No es necesario declararla en el método con "throws"
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor que recibe el mensaje de error
     * @param message Mensaje descriptivo del error
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe mensaje y causa del error
     * @param message Mensaje descriptivo
     * @param cause Excepción que causó este error
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}