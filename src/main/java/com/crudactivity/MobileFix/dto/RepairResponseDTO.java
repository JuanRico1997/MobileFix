package com.crudactivity.MobileFix.dto;

import com.crudactivity.MobileFix.model.Status;

import java.time.LocalDate;

/**
 * DTO para enviar información de la Reparación al cliente
 * Se usa en respuestas de GET
 */
public class RepairResponseDTO {

    private Long id;
    private String description;
    private LocalDate requestDate;
    private LocalDate estimatedDate;
    private Status status;
    private Double cost;

    // Información del dispositivo
    private Long deviceId;
    private String deviceBrand;
    private String deviceModel;

    // Información del técnico (puede ser null)
    private Long technicianId;
    private String technicianUsername;

    // Constructor vacío
    public RepairResponseDTO() {
    }

    // Constructor completo
    public RepairResponseDTO(Long id, String description, LocalDate requestDate,
                             LocalDate estimatedDate, Status status, Double cost,
                             Long deviceId, String deviceBrand, String deviceModel,
                             Long technicianId, String technicianUsername) {
        this.id = id;
        this.description = description;
        this.requestDate = requestDate;
        this.estimatedDate = estimatedDate;
        this.status = status;
        this.cost = cost;
        this.deviceId = deviceId;
        this.deviceBrand = deviceBrand;
        this.deviceModel = deviceModel;
        this.technicianId = technicianId;
        this.technicianUsername = technicianUsername;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
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

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    public String getTechnicianUsername() {
        return technicianUsername;
    }

    public void setTechnicianUsername(String technicianUsername) {
        this.technicianUsername = technicianUsername;
    }
}