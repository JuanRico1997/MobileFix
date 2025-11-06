package com.crudactivity.MobileFix.dto;

/**
 * DTO para enviar información del Dispositivo al cliente
 * Se usa en respuestas de GET
 */
public class DeviceResponseDTO {

    private Long id;
    private String brand;
    private String model;
    private Long ownerId;
    private String ownerUsername; // Nombre del propietario para mostrar

    // Constructor vacío
    public DeviceResponseDTO() {
    }

    // Constructor con todos los campos
    public DeviceResponseDTO(Long id, String brand, String model, Long ownerId, String ownerUsername) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}