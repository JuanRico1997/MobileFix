package com.crudactivity.MobileFix.dto;

/**
 * DTO para recibir información del Dispositivo desde el cliente
 * Se usa para CREATE y UPDATE
 */
public class DeviceRequestDTO {

    private String brand;
    private String model;
    private Long ownerId; // ID del usuario propietario

    // Constructor vacío
    public DeviceRequestDTO() {
    }

    // Constructor con todos los campos
    public DeviceRequestDTO(String brand, String model, Long ownerId) {
        this.brand = brand;
        this.model = model;
        this.ownerId = ownerId;
    }

    // Getters y Setters
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}