package com.crudactivity.MobileFix.service;


import com.crudactivity.MobileFix.dto.UserRequestDTO;
import com.crudactivity.MobileFix.dto.UserResponseDTO;
import com.crudactivity.MobileFix.exception.ResourceNotFoundException;
import com.crudactivity.MobileFix.model.Role;
import com.crudactivity.MobileFix.model.User;
import com.crudactivity.MobileFix.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Crear un nuevo usuario - Valida que username y email sean unicos
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        //Valida que el username no exista
        if(userRepository.existsByUserName(userRequestDTO.getUsername())){
            throw new IllegalArgumentException(
                    "El nombre de usuario "  + userRequestDTO.getUsername() + " ya existe"
            );
        }
        if(userRepository.existsByEmail(userRequestDTO.getEmail())){
            throw new IllegalArgumentException(
                    "El email "  + userRequestDTO.getEmail() + " ya existe"
            );
        }

        //Convertir DTO a Entity
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setRole(userRequestDTO.getRole());

        //Guardar en bd
        User savedUser = userRepository.save(user);

        //Convertir entity a ResponseDTO
        return convertToResponseDTO(savedUser);

    }

    //Obtener un User por su ID
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id){
        User user =  userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID" + id
                ));
        return convertToResponseDTO(user);
    }

    //Buscar usuario por username
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con username: " + username
                ));
        return convertToResponseDTO(user);
    }

    //Buscar usuario por email
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con email: " + email
                ));
        return convertToResponseDTO(user);
    }

    //Buscar todos los usuarios por rol
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    //Obtener todos los usuarios
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    //Actualizar un usuario existente
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        // Buscar el usuario existente
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + id
                ));

        // Validar username único (si cambió)
        if (!existingUser.getUsername().equals(userRequestDTO.getUsername())) {
            if (userRepository.existsByUserName(userRequestDTO.getUsername())) {
                throw new IllegalArgumentException(
                        "El nombre de usuario '" + userRequestDTO.getUsername() + "' ya existe"
                );
            }
        }

        // Validar email único (si cambió)
        if (!existingUser.getEmail().equals(userRequestDTO.getEmail())) {
            if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
                throw new IllegalArgumentException(
                        "El email '" + userRequestDTO.getEmail() + "' ya está registrado"
                );
            }
        }

        // Actualizar los campos
        existingUser.setUsername(userRequestDTO.getUsername());
        existingUser.setEmail(userRequestDTO.getEmail());

        // Solo actualizar password si viene en el request
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
            existingUser.setPassword(userRequestDTO.getPassword());
        }

        existingUser.setRole(userRequestDTO.getRole());

        // Guardar cambios
        User updatedUser = userRepository.save(existingUser);

        return convertToResponseDTO(updatedUser);
    }

    //Eliminar usuario por ID
    public void deleteUser(Long id) {
        // Verificar que el usuario existe
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Usuario no encontrado con ID: " + id
            );
        }

        userRepository.deleteById(id);
    }

    //Verificar si existe un usuario con un username específico
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    //Verificar si existe un usuario con un email específico
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    //Convierte un Entity User a UserResponseDTO
    //NO incluye password por seguridad
    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }




}
