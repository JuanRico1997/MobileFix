package com.crudactivity.MobileFix.dto;

import com.crudactivity.MobileFix.model.Role;

/**
 * DTO para recibir información del Usuario desde el cliente
 * Se usa para CREATE y UPDATE
 */
public class UserRequestDTO {

    private String username;
    private String email;
    private String password;
    private Role role;

    // Constructor vacío
    public UserRequestDTO() {
    }

    // Constructor con todos los campos
    public UserRequestDTO(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters y Setters
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
}