package com.crudactivity.MobileFix.service;

import com.crudactivity.MobileFix.dto.RepairRequestDTO;
import com.crudactivity.MobileFix.dto.RepairResponseDTO;
import com.crudactivity.MobileFix.exception.ResourceNotFoundException;
import com.crudactivity.MobileFix.model.Device;
import com.crudactivity.MobileFix.model.Repair;
import com.crudactivity.MobileFix.model.Status;
import com.crudactivity.MobileFix.model.User;
import com.crudactivity.MobileFix.repositories.DeviceRepository;
import com.crudactivity.MobileFix.repositories.RepairRepository;
import com.crudactivity.MobileFix.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para manejar la lógica de negocio de Repairs
 * Implementa operaciones CRUD y conversiones entre Entity y DTO
 */
@Service
@Transactional
public class RepairService {

    private final RepairRepository repairRepository;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Autowired
    public RepairService(RepairRepository repairRepository,
                         DeviceRepository deviceRepository,
                         UserRepository userRepository) {
        this.repairRepository = repairRepository;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crear una nueva reparación
     * Valida que el dispositivo exista y que el técnico (si se asigna) exista
     *
     * @param repairRequestDTO datos de la reparación a crear
     * @return RepairResponseDTO con los datos de la reparación creada
     * @throws ResourceNotFoundException si el dispositivo o técnico no existen
     */
    public RepairResponseDTO createRepair(RepairRequestDTO repairRequestDTO) {
        // Buscar el dispositivo
        Device device = deviceRepository.findById(repairRequestDTO.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dispositivo no encontrado con ID: " + repairRequestDTO.getDeviceId()
                ));

        // Crear la reparación
        Repair repair = new Repair();
        repair.setDescription(repairRequestDTO.getDescription());
        repair.setEstimatedDate(repairRequestDTO.getEstimatedDate());
        repair.setCost(repairRequestDTO.getCost());
        repair.setDevice(device);

        // Establecer estado (si viene en el request, sino por defecto PENDING)
        if (repairRequestDTO.getStatus() != null) {
            repair.setStatus(repairRequestDTO.getStatus());
        } else {
            repair.setStatus(Status.PENDING);
        }

        // Asignar técnico si viene en el request
        if (repairRequestDTO.getTechnicianId() != null) {
            User technician = userRepository.findById(repairRequestDTO.getTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Técnico no encontrado con ID: " + repairRequestDTO.getTechnicianId()
                    ));
            repair.setTechnician(technician);
        }

        // Guardar en BD (requestDate se establece automáticamente con @PrePersist)
        Repair savedRepair = repairRepository.save(repair);

        return convertToResponseDTO(savedRepair);
    }

    /**
     * Obtener todas las reparaciones
     *
     * @return Lista de RepairResponseDTO
     */
    @Transactional(readOnly = true)
    public List<RepairResponseDTO> getAllRepairs() {
        return repairRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener una reparación por su ID
     *
     * @param id ID de la reparación
     * @return RepairResponseDTO con los datos de la reparación
     * @throws ResourceNotFoundException si no se encuentra la reparación
     */
    @Transactional(readOnly = true)
    public RepairResponseDTO getRepairById(Long id) {
        Repair repair = repairRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reparación no encontrada con ID: " + id
                ));
        return convertToResponseDTO(repair);
    }

    /**
     * Obtener todas las reparaciones de un dispositivo específico
     *
     * @param deviceId ID del dispositivo
     * @return Lista de RepairResponseDTO
     */
    @Transactional(readOnly = true)
    public List<RepairResponseDTO> getRepairsByDeviceId(Long deviceId) {
        // Verificar que el dispositivo existe
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException(
                    "Dispositivo no encontrado con ID: " + deviceId
            );
        }

        return repairRepository.findByDeviceId(deviceId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todas las reparaciones por estado
     *
     * @param status estado de la reparación
     * @return Lista de RepairResponseDTO
     */
    @Transactional(readOnly = true)
    public List<RepairResponseDTO> getRepairsByStatus(Status status) {
        return repairRepository.findByStatus(status)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todas las reparaciones de un técnico específico
     *
     * @param technicianId ID del técnico
     * @return Lista de RepairResponseDTO
     */
    @Transactional(readOnly = true)
    public List<RepairResponseDTO> getRepairsByTechnicianId(Long technicianId) {
        // Verificar que el técnico existe
        if (!userRepository.existsById(technicianId)) {
            throw new ResourceNotFoundException(
                    "Técnico no encontrado con ID: " + technicianId
            );
        }

        return repairRepository.findByTechnicianId(technicianId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todas las reparaciones de los dispositivos de un propietario
     *
     * @param ownerId ID del propietario
     * @return Lista de RepairResponseDTO
     */
    @Transactional(readOnly = true)
    public List<RepairResponseDTO> getRepairsByOwnerId(Long ownerId) {
        // Verificar que el propietario existe
        if (!userRepository.existsById(ownerId)) {
            throw new ResourceNotFoundException(
                    "Propietario no encontrado con ID: " + ownerId
            );
        }

        return repairRepository.findByDeviceOwnerId(ownerId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener reparaciones sin técnico asignado
     *
     * @return Lista de RepairResponseDTO
     */
    @Transactional(readOnly = true)
    public List<RepairResponseDTO> getUnassignedRepairs() {
        return repairRepository.findByTechnicianIsNull()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener reparaciones con técnico asignado
     *
     * @return Lista de RepairResponseDTO
     */
    @Transactional(readOnly = true)
    public List<RepairResponseDTO> getAssignedRepairs() {
        return repairRepository.findByTechnicianIsNotNull()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener reparaciones entre dos fechas
     *
     * @param startDate fecha de inicio
     * @param endDate   fecha de fin
     * @return Lista de RepairResponseDTO
     */
    @Transactional(readOnly = true)
    public List<RepairResponseDTO> getRepairsByDateRange(LocalDate startDate, LocalDate endDate) {
        return repairRepository.findByRequestDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener el costo total de todas las reparaciones completadas de un dispositivo
     *
     * @param deviceId ID del dispositivo
     * @return costo total
     */
    @Transactional(readOnly = true)
    public Double getTotalCostByDevice(Long deviceId) {
        // Verificar que el dispositivo existe
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException(
                    "Dispositivo no encontrado con ID: " + deviceId
            );
        }

        Double total = repairRepository.getTotalCostByDevice(deviceId);
        return total != null ? total : 0.0;
    }

    /**
     * Actualizar una reparación existente
     *
     * @param id                ID de la reparación a actualizar
     * @param repairRequestDTO datos actualizados
     * @return RepairResponseDTO con los datos actualizados
     * @throws ResourceNotFoundException si no se encuentra la reparación, dispositivo o técnico
     */
    public RepairResponseDTO updateRepair(Long id, RepairRequestDTO repairRequestDTO) {
        // Buscar la reparación existente
        Repair existingRepair = repairRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reparación no encontrada con ID: " + id
                ));

        // Si cambió el dispositivo, verificar que el nuevo dispositivo existe
        if (!existingRepair.getDevice().getId().equals(repairRequestDTO.getDeviceId())) {
            Device newDevice = deviceRepository.findById(repairRequestDTO.getDeviceId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Dispositivo no encontrado con ID: " + repairRequestDTO.getDeviceId()
                    ));
            existingRepair.setDevice(newDevice);
        }

        // Actualizar técnico
        if (repairRequestDTO.getTechnicianId() != null) {
            User technician = userRepository.findById(repairRequestDTO.getTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Técnico no encontrado con ID: " + repairRequestDTO.getTechnicianId()
                    ));
            existingRepair.setTechnician(technician);
        } else {
            // Si viene null, se desasigna el técnico
            existingRepair.setTechnician(null);
        }

        // Actualizar los demás campos
        existingRepair.setDescription(repairRequestDTO.getDescription());
        existingRepair.setEstimatedDate(repairRequestDTO.getEstimatedDate());
        existingRepair.setStatus(repairRequestDTO.getStatus());
        existingRepair.setCost(repairRequestDTO.getCost());

        // Guardar cambios
        Repair updatedRepair = repairRepository.save(existingRepair);

        return convertToResponseDTO(updatedRepair);
    }

    /**
     * Asignar un técnico a una reparación
     *
     * @param repairId      ID de la reparación
     * @param technicianId ID del técnico
     * @return RepairResponseDTO actualizado
     */
    public RepairResponseDTO assignTechnician(Long repairId, Long technicianId) {
        Repair repair = repairRepository.findById(repairId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reparación no encontrada con ID: " + repairId
                ));

        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Técnico no encontrado con ID: " + technicianId
                ));

        repair.setTechnician(technician);

        // Cambiar estado a EN_PROCESO si estaba PENDING
        if (repair.getStatus() == Status.PENDING) {
            repair.setStatus(Status.IN_PROGRESS);
        }

        Repair updatedRepair = repairRepository.save(repair);
        return convertToResponseDTO(updatedRepair);
    }

    /**
     * Cambiar el estado de una reparación
     *
     * @param repairId  ID de la reparación
     * @param newStatus nuevo estado
     * @return RepairResponseDTO actualizado
     */
    public RepairResponseDTO updateStatus(Long repairId, Status newStatus) {
        Repair repair = repairRepository.findById(repairId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reparación no encontrada con ID: " + repairId
                ));

        repair.setStatus(newStatus);
        Repair updatedRepair = repairRepository.save(repair);
        return convertToResponseDTO(updatedRepair);
    }

    /**
     * Eliminar una reparación por ID
     *
     * @param id ID de la reparación a eliminar
     * @throws ResourceNotFoundException si no se encuentra la reparación
     */
    public void deleteRepair(Long id) {
        // Verificar que la reparación existe
        if (!repairRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Reparación no encontrada con ID: " + id
            );
        }

        repairRepository.deleteById(id);
    }

    // ============== MÉTODOS AUXILIARES ==============

    /**
     * Convierte un Entity Repair a RepairResponseDTO
     *
     * @param repair entidad Repair
     * @return RepairResponseDTO
     */
    private RepairResponseDTO convertToResponseDTO(Repair repair) {
        Device device = repair.getDevice();
        User technician = repair.getTechnician();

        return new RepairResponseDTO(
                repair.getId(),
                repair.getDescription(),
                repair.getRequestDate(),
                repair.getEstimatedDate(),
                repair.getStatus(),
                repair.getCost(),
                device.getId(),
                device.getBrand(),
                device.getModel(),
                technician != null ? technician.getId() : null,
                technician != null ? technician.getUsername() : null
        );
    }
}