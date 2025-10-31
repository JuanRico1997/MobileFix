package com.crudactivity.MobileFix.model;

public enum Status {
    PENDING,        // Reparación recién creada, esperando asignación
    IN_PROGRESS,    // Reparación en progreso
    COMPLETED,      // Reparación finalizada exitosamente
    CANCELLED       // Reparación cancelada
}
