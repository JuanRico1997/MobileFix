package com.crudactivity.MobileFix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 * Entidad User - Representa un usuario del sistema
 * Puede ser: ADMIN, USER, TECNICO o CLIENTE
 *
 * Mapea con tabla 'users' en H2
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único en el sistema
     * Validación: no vacío, longitud entre 3 y 50 caracteres
     */
    @Column(unique = true, nullable = false, length = 50)
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String username;

    /**
     * Email único del usuario
     * Validación: formato de email válido
     */
    @Column(unique = true, nullable = false)
    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    /**
     * Contraseña del usuario
     * En producción debería estar hasheada (BCrypt)
     */
    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    /**
     * Rol del usuario en el sistema
     * Se almacena como String en BD pero se usa como Enum en código
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /**
     * Relación One-to-Many con Device
     * Un usuario puede tener varios dispositivos
     * CascadeType.ALL: si eliminamos un User, se eliminan sus Device
     * orphanRemoval: si quitamos un Device de la lista, se elimina de BD
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> devices;

    // Constructores
    public User() {
    }

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}