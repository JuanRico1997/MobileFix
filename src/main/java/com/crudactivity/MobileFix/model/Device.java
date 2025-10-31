package com.crudactivity.MobileFix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 * Entidad Device - Representa un dispositivo móvil
 * Pertenece a un User (owner) y puede tener varias reparaciones
 *
 * Mapea con tabla 'devices' en H2
 */
@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Marca del dispositivo
     * Ejemplo: Samsung, Apple, Xiaomi, Huawei
     */
    @Column(nullable = false, length = 50)
    @NotBlank(message = "La marca es obligatoria")
    private String brand;

    /**
     * Modelo específico del dispositivo
     * Ejemplo: Galaxy S21, iPhone 13, Redmi Note 10
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "El modelo es obligatorio")
    private String model;


    /**
     * Relación Many-to-One con User
     * Muchos dispositivos pueden pertenecer a un mismo usuario
     * fetch = LAZY: no carga el owner hasta que se necesite (optimización)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    /**
     * Relación One-to-Many con Repair
     * Un dispositivo puede tener varias reparaciones
     */
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Repair> repairs;

    // Constructores
    public Device() {
    }

    public Device(String brand, String model, User owner) {
        this.brand = brand;
        this.model = model;
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Repair> getRepairs() {
        return repairs;
    }

    public void setRepairs(List<Repair> repairs) {
        this.repairs = repairs;
    }
}