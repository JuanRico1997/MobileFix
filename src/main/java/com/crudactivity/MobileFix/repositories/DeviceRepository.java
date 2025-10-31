package com.crudactivity.MobileFix.repositories;

import com.crudactivity.MobileFix.model.Device;
import com.crudactivity.MobileFix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Long> {

    List<Device> findByOwner(User owner);

    List<Device> findByOwnerId(Long ownerId);

    List<Device> findByBrand(String brand);

    List<Device> findByBrandAndModel(String brand,String model);

    Long countByOwner(User owner);

}
