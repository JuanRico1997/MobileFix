package com.crudactivity.MobileFix.service;

import com.crudactivity.MobileFix.dto.DeviceRequestDTO;
import com.crudactivity.MobileFix.dto.DeviceResponseDTO;
import com.crudactivity.MobileFix.exception.ResourceNotFoundException;
import com.crudactivity.MobileFix.model.Device;
import com.crudactivity.MobileFix.model.User;
import com.crudactivity.MobileFix.repositories.DeviceRepository;
import com.crudactivity.MobileFix.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para manejar la lógica de negocio de Devices
 * Implementa operaciones CRUD y conversiones entre Entity y DTO
 */
@Service
@Transactional
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crear un nuevo dispositivo
     * Valida que el propietario exista
     *
     * @param deviceRequestDTO datos del dispositivo a crear
     * @return DeviceResponseDTO con los datos del dispositivo creado
     * @throws ResourceNotFoundException si el propietario no existe
     */
    public DeviceResponseDTO createDevice(DeviceRequestDTO deviceRequestDTO) {
        // Buscar el propietario
        User owner = userRepository.findById(deviceRequestDTO.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario propietario no encontrado con ID: " + deviceRequestDTO.getOwnerId()
                ));

        // Crear el dispositivo
        Device device = new Device();
        device.setBrand(deviceRequestDTO.getBrand());
        device.setModel(deviceRequestDTO.getModel());
        device.setOwner(owner);

        // Guardar en BD
        Device savedDevice = deviceRepository.save(device);

        return convertToResponseDTO(savedDevice);
    }

    /**
     * Obtener todos los dispositivos
     *
     * @return Lista de DeviceResponseDTO
     */
    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> getAllDevices() {
        return deviceRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener un dispositivo por su ID
     *
     * @param id ID del dispositivo
     * @return DeviceResponseDTO con los datos del dispositivo
     * @throws ResourceNotFoundException si no se encuentra el dispositivo
     */
    @Transactional(readOnly = true)
    public DeviceResponseDTO getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dispositivo no encontrado con ID: " + id
                ));
        return convertToResponseDTO(device);
    }

    /**
     * Obtener todos los dispositivos de un propietario específico
     *
     * @param ownerId ID del propietario
     * @return Lista de DeviceResponseDTO
     */
    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> getDevicesByOwnerId(Long ownerId) {
        // Verificar que el propietario existe
        if (!userRepository.existsById(ownerId)) {
            throw new ResourceNotFoundException(
                    "Usuario propietario no encontrado con ID: " + ownerId
            );
        }

        return deviceRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener dispositivos por marca
     *
     * @param brand marca del dispositivo
     * @return Lista de DeviceResponseDTO
     */
    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> getDevicesByBrand(String brand) {
        return deviceRepository.findByBrand(brand)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener dispositivos por marca y modelo
     *
     * @param brand marca del dispositivo
     * @param model modelo del dispositivo
     * @return Lista de DeviceResponseDTO
     */
    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> getDevicesByBrandAndModel(String brand, String model) {
        return deviceRepository.findByBrandAndModel(brand, model)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Contar cuántos dispositivos tiene un propietario
     *
     * @param ownerId ID del propietario
     * @return número de dispositivos
     */
    @Transactional(readOnly = true)
    public Long countDevicesByOwnerId(Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario propietario no encontrado con ID: " + ownerId
                ));
        return deviceRepository.countByOwner(owner);
    }

    /**
     * Actualizar un dispositivo existente
     *
     * @param id                ID del dispositivo a actualizar
     * @param deviceRequestDTO datos actualizados
     * @return DeviceResponseDTO con los datos actualizados
     * @throws ResourceNotFoundException si no se encuentra el dispositivo o el nuevo propietario
     */
    public DeviceResponseDTO updateDevice(Long id, DeviceRequestDTO deviceRequestDTO) {
        // Buscar el dispositivo existente
        Device existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dispositivo no encontrado con ID: " + id
                ));

        // Si cambió el propietario, verificar que el nuevo propietario existe
        if (!existingDevice.getOwner().getId().equals(deviceRequestDTO.getOwnerId())) {
            User newOwner = userRepository.findById(deviceRequestDTO.getOwnerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Usuario propietario no encontrado con ID: " + deviceRequestDTO.getOwnerId()
                    ));
            existingDevice.setOwner(newOwner);
        }

        // Actualizar los campos
        existingDevice.setBrand(deviceRequestDTO.getBrand());
        existingDevice.setModel(deviceRequestDTO.getModel());

        // Guardar cambios
        Device updatedDevice = deviceRepository.save(existingDevice);

        return convertToResponseDTO(updatedDevice);
    }

    /**
     * Eliminar un dispositivo por ID
     * NOTA: Si el dispositivo tiene reparaciones, también se eliminarán (cascade)
     *
     * @param id ID del dispositivo a eliminar
     * @throws ResourceNotFoundException si no se encuentra el dispositivo
     */
    public void deleteDevice(Long id) {
        // Verificar que el dispositivo existe
        if (!deviceRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Dispositivo no encontrado con ID: " + id
            );
        }

        deviceRepository.deleteById(id);
    }

    // ============== MÉTODOS AUXILIARES ==============

    /**
     * Convierte un Entity Device a DeviceResponseDTO
     *
     * @param device entidad Device
     * @return DeviceResponseDTO
     */
    private DeviceResponseDTO convertToResponseDTO(Device device) {
        return new DeviceResponseDTO(
                device.getId(),
                device.getBrand(),
                device.getModel(),
                device.getOwner().getId(),
                device.getOwner().getUsername()
        );
    }
}