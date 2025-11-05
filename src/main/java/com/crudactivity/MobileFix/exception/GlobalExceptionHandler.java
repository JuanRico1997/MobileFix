package com.crudactivity.MobileFix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones
 * Captura todas las excepciones lanzadas en los Controllers
 * y las convierte en respuestas JSON con el formato adecuado
 *
 * @RestControllerAdvice combina @ControllerAdvice + @ResponseBody
 * Esto significa que captura excepciones y devuelve JSON automáticamente
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja ResourceNotFoundException (404 Not Found)
     * Se ejecuta cuando lanzamos: throw new ResourceNotFoundException(...)
     *
     * @param ex La excepción lanzada
     * @return ResponseEntity con los detalles del error y status 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        // Crear un mapa con los detalles del error
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        errorDetails.put("error", "Not Found");

        // Devolver respuesta con status 404
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja cualquier otra excepción no capturada (500 Internal Server Error)
     * Este es el "catch-all" para errores inesperados
     *
     * @param ex La excepción lanzada
     * @return ResponseEntity con los detalles del error y status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "Error interno del servidor");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetails.put("error", "Internal Server Error");

        // Devolver respuesta con status 500
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}