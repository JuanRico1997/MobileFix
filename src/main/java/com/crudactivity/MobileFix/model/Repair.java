package com.crudactivity.MobileFix.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Entidad Repair - Representa una reparación de dispositivo móvil
 * Asociada a un Device y opcionalmente a un técnico (User con role TECNICO)
 *
 * Mapea con tabla 'repairs' en H2
 */
@Entity
@Table(name = "repairs")
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Descripción del problema o reparación solicitada
     * Ejemplo: "Pantalla rota", "Batería no carga", "Problemas con cámara"
     */
    @Column(nullable = false, length = 500)
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    private String description;

    /**
     * Fecha en que se solicitó la reparación
     * Se establece automáticamente al crear el registro
     */
    @Column(nullable = false)
    private LocalDate requestDate;

    /**
     * Fecha estimada de finalización de la reparación
     * Puede ser null si aún no se ha estimado
     */
    @Column
    private LocalDate estimatedDate;

    /**
     * Estado actual de la reparación
     * Valores posibles: PENDIENTE, EN_PROCESO, COMPLETADO, CANCELADO
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    /**
     * Costo de la reparación en la moneda local
     * Debe ser un valor positivo
     */
    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo debe ser mayor a 0")
    private Double cost;

    /**
     * Relación Many-to-One con Device
     * Muchas reparaciones pueden ser del mismo dispositivo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    /**
     * Relación Many-to-One con User (técnico asignado)
     * Muchas reparaciones pueden ser atendidas por el mismo técnico
     * Puede ser null si aún no se ha asignado técnico
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private User technician;

    /**
     * Método que se ejecuta antes de persistir la entidad
     * Establece la fecha de solicitud automáticamente
     */
    @PrePersist
    protected void onCreate() {
        if (requestDate == null) {
            requestDate = LocalDate.now();
        }
        if (status == null) {
            status = Status.PENDING;
        }
    }

    // Constructores
    public Repair() {
    }

    public Repair(String description, LocalDate estimatedDate, Double cost, Device device) {
        this.description = description;
        this.estimatedDate = estimatedDate;
        this.cost = cost;
        this.device = device;
        this.status = Status.PENDING;
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public User getTechnician() {
        return technician;
    }

    public void setTechnician(User technician) {
        this.technician = technician;
    }
}