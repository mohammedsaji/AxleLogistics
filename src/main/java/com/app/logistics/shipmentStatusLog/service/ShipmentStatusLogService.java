// shipmentStatusLog/service/ShipmentStatusLogService.java
package com.app.logistics.shipmentStatusLog.service;

import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.shipmentStatusLog.repo.StatusLogRepo;
import com.app.logistics.shipmentStatusLog.entity.ShipmentStatusLog;
import com.app.logistics.shipmentStatus.entity.ShipmentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentStatusLogService {

    private final StatusLogRepo statusLogRepo;

    public ShipmentStatusLogService(StatusLogRepo statusLogRepo) {
        this.statusLogRepo = statusLogRepo;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveStatusLog(ShipmentStatus savedShipmentStatus, Cargo cargo) {
        if (savedShipmentStatus == null) {
            return;
        }
        ShipmentStatusLog savingShipmentStatusLog = statusVOTostatusLogVO(savedShipmentStatus, cargo);
        statusLogRepo.save(savingShipmentStatusLog);
    }

    public ShipmentStatusLog statusVOTostatusLogVO(ShipmentStatus savedShipmentStatus, Cargo cargo) {
        ShipmentStatusLog shipmentStatusLog = new ShipmentStatusLog();
        shipmentStatusLog.setShipmentStatusVO(savedShipmentStatus);
        shipmentStatusLog.setShippingStatus(savedShipmentStatus.getShippingStatus());
        shipmentStatusLog.setCurrentLocation(savedShipmentStatus.getCurrentLocation());
        shipmentStatusLog.setShippingCargoVO(cargo);
        shipmentStatusLog.setShippingOperatorVO(savedShipmentStatus.getShippingOperatorVO());
        shipmentStatusLog.setShippingDriverVO(savedShipmentStatus.getShippingDriverVO());
        shipmentStatusLog.setShippingVehicleVO(savedShipmentStatus.getShippingVehicleVO());
        shipmentStatusLog.setUpdatedAt(savedShipmentStatus.getUpdatedAt());
        shipmentStatusLog.setUpdatedby(savedShipmentStatus.getUpdatedby());
        return shipmentStatusLog;
    }
}