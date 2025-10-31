package com.crudactivity.MobileFix.repositories;


import com.crudactivity.MobileFix.model.Device;
import com.crudactivity.MobileFix.model.Repair;
import com.crudactivity.MobileFix.model.Status;
import com.crudactivity.MobileFix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<Repair,Long> {

    List<Repair> findByDevice(Device device);

    List<Repair> findByDeviceId(Long deviceId);

    List<Repair> findByStatus(Status status);

    List<Repair> findByTechnician(User technician);

    List<Repair> findByTechnicianId(Long technicianId);

    @Query("SELECT r FROM Repair r WHERE r.device.owner.id = :ownerId")
    List<Repair> findByDeviceOwnerId(@Param("ownerId") Long ownerId);

    List<Repair> findByStatusAndTechnician(Status status, User technician);

    List<Repair> findByRequestDateBetween(LocalDate startDate, LocalDate endDate);

    List<Repair> findByTechnicianIsNull();

    List<Repair> findByTechnicianIsNotNull();

    Long countByTechnicianAndStatus(User technician, Status status);

    List<Repair> findByStatusOrderByRequestDateDesc(Status status);

    @Query("SELECT SUM(r.cost) FROM Repair r WHERE r.device.id = :deviceId AND r.status = 'COMPLETED'")
    Double getTotalCostByDevice(@Param("deviceId") Long deviceId);

    List<Repair> findAllByOrderByCostDesc();


}
