package com.crudactivity.MobileFix.dto;

import com.crudactivity.MobileFix.model.Status;

import java.time.LocalDate;

/**
 * DTO para recibir información de la Reparación desde el cliente
 * Se usa para CREATE y UPDATE
 */
public class RepairRequestDTO {

    private String description;
    private LocalDate estimatedDate;
    private Status status;
    private Double cost;
    private Long deviceId; // ID del dispositivo a reparar
    private Long technicianId; // ID del técnico asignado (puede ser null)

    // Constructor vacío
    public RepairRequestDTO() {
    }

    // Constructor con todos los campos
    public RepairRequestDTO(String description, LocalDate estimatedDate, Status status,
                            Double cost, Long deviceId, Long technicianId) {
        this.description = description;
        this.estimatedDate = estimatedDate;
        this.status = status;
        this.cost = cost;
        this.deviceId = deviceId;
        this.technicianId = technicianId;
    }

    // Getters y Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(LocalDate estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }
}