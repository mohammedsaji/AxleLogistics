package com.app.logistics.shipmentStatusLog.service;

import com.app.logistics.shipmentStatusLog.repo.StatusLogRepo;
import com.app.logistics.shipmentStatusLog.entity.ShipmentStatusLog;
import com.app.logistics.shipmentStatus.entity.ShipmentStatus;
import com.app.logistics.shipment.entity.Shipment;
import com.app.logistics.shipment.service.ShipmentService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentStatusLogService {

    private final StatusLogRepo statusLogRepo;

    private final ShipmentService shipmentService;

    public ShipmentStatusLogService(StatusLogRepo statusLogRepo,
                                    @Lazy ShipmentService shipmentService){
        this.statusLogRepo = statusLogRepo;
        this.shipmentService = shipmentService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveStatusLog(ShipmentStatus savedShipmentStatus) {
        if (savedShipmentStatus == null) {
            return;
        }
        ShipmentStatusLog savingShipmentStatusLog = statusVOTostatusLogVO(savedShipmentStatus);
        if (savingShipmentStatusLog != null) {
            statusLogRepo.save(savingShipmentStatusLog);
        }
    }

    public ShipmentStatusLog statusVOTostatusLogVO(ShipmentStatus savedShipmentStatus){
        if (savedShipmentStatus == null) {
            return new ShipmentStatusLog();
        }

        Shipment getShipment = shipmentService.internalFetchService(savedShipmentStatus.getShippingStatusId());

        ShipmentStatusLog shipmentStatusLog = new ShipmentStatusLog();
        shipmentStatusLog.setShipmentStatusVO(savedShipmentStatus);
        shipmentStatusLog.setShippingStatus(savedShipmentStatus.getShippingStatus());
        shipmentStatusLog.setCurrentLocation(savedShipmentStatus.getCurrentLocation());
        if(getShipment != null) {
            shipmentStatusLog.setShippingCargoVO(getShipment.getShippingCargoInfoVO());
        }
        shipmentStatusLog.setShippingOperatorVO(savedShipmentStatus.getShippingOperatorVO());
        shipmentStatusLog.setShippingDriverVO(savedShipmentStatus.getShippingDriverVO());
        shipmentStatusLog.setShippingVehicleVO(savedShipmentStatus.getShippingVehicleVO());
        shipmentStatusLog.setUpdatedAt(savedShipmentStatus.getUpdatedAt());
        shipmentStatusLog.setUpdatedby(savedShipmentStatus.getUpdatedby());
        return shipmentStatusLog;
    }
}
