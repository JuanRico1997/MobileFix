package com.crudactivity.MobileFix.controller;

import com.crudactivity.MobileFix.dto.UserRequestDTO;
import com.crudactivity.MobileFix.dto.UserResponseDTO;
import com.crudactivity.MobileFix.model.Role;
import com.crudactivity.MobileFix.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar Users
 * Endpoint base: /api/users
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Permitir peticiones desde cualquier origen
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /api/users
     * Crear un nuevo usuario
     *
     * @param userRequestDTO datos del usuario a crear
     * @return 201 CREATED con el usuario creado
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * GET /api/users
     * Obtener todos los usuarios
     *
     * @return 200 OK con lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/users/{id}
     * Obtener un usuario por ID
     *
     * @param id ID del usuario
     * @return 200 OK con el usuario o 404 NOT FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * GET /api/users/username/{username}
     * Buscar usuario por username
     *
     * @param username nombre de usuario
     * @return 200 OK con el usuario o 404 NOT FOUND
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        UserResponseDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * GET /api/users/email/{email}
     * Buscar usuario por email
     *
     * @param email correo electrónico
     * @return 200 OK con el usuario o 404 NOT FOUND
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * GET /api/users/role/{role}
     * Obtener usuarios por rol
     *
     * @param role rol del usuario (ADMIN, USER, TECNICO, CLIENTE)
     * @return 200 OK con lista de usuarios
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable Role role) {
        List<UserResponseDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * PUT /api/users/{id}
     * Actualizar un usuario existente
     *
     * @param id             ID del usuario a actualizar
     * @param userRequestDTO datos actualizados
     * @return 200 OK con el usuario actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * DELETE /api/users/{id}
     * Eliminar un usuario
     *
     * @param id ID del usuario a eliminar
     * @return 200 OK con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario eliminado exitosamente");
        response.put("id", id.toString());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/users/exists/username/{username}
     * Verificar si existe un username
     *
     * @param username nombre de usuario
     * @return 200 OK con true/false
     */
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Map<String, Boolean>> existsByUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/users/exists/email/{email}
     * Verificar si existe un email
     *
     * @param email correo electrónico
     * @return 200 OK con true/false
     */
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Map<String, Boolean>> existsByEmail(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }
}