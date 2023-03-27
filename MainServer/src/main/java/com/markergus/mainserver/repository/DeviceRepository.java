package com.markergus.mainserver.repository;

import com.markergus.mainserver.entity.Device;
import org.springframework.data.repository.CrudRepository;

public interface DeviceRepository extends CrudRepository<Device, Long> {

    public boolean existsByDeviceId(String deviceId);
}
