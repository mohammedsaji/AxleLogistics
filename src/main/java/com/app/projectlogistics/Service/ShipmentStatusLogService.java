package com.app.projectlogistics.Service;

import com.app.projectlogistics.Repository.StatusLogRepo;
import com.app.projectlogistics.ValueObject.ShipmentStatusLogVO;
import com.app.projectlogistics.ValueObject.ShipmentStatusVO;
import com.app.projectlogistics.ValueObject.ShipmentVO;
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
    public void saveStatusLog(ShipmentStatusVO savedShipmentStatusVO) {
        if (savedShipmentStatusVO == null) {
            return;
        }
        ShipmentStatusLogVO savingShipmentStatusLogVO = statusVOTostatusLogVO(savedShipmentStatusVO);
        if (savingShipmentStatusLogVO != null) {
            statusLogRepo.save(savingShipmentStatusLogVO);
        }
    }

    public ShipmentStatusLogVO statusVOTostatusLogVO(ShipmentStatusVO savedShipmentStatusVO){
        if (savedShipmentStatusVO == null) {
            return new ShipmentStatusLogVO();
        }

        ShipmentVO getShipmentVO = shipmentService.internalFetchService(savedShipmentStatusVO.getShippingStatusId());

        ShipmentStatusLogVO shipmentStatusLogVO = new ShipmentStatusLogVO();
        shipmentStatusLogVO.setShipmentStatusVO(savedShipmentStatusVO);
        shipmentStatusLogVO.setShippingStatus(savedShipmentStatusVO.getShippingStatus());
        shipmentStatusLogVO.setCurrentLocation(savedShipmentStatusVO.getCurrentLocation());
        if(getShipmentVO != null) {
            shipmentStatusLogVO.setShippingCargoVO(getShipmentVO.getShippingCargoInfoVO());
        }
        shipmentStatusLogVO.setShippingOperatorVO(savedShipmentStatusVO.getShippingOperatorVO());
        shipmentStatusLogVO.setShippingDriverVO(savedShipmentStatusVO.getShippingDriverVO());
        shipmentStatusLogVO.setShippingVehicleVO(savedShipmentStatusVO.getShippingVehicleVO());
        shipmentStatusLogVO.setUpdatedAt(savedShipmentStatusVO.getUpdatedAt());
        shipmentStatusLogVO.setUpdatedby(savedShipmentStatusVO.getUpdatedby());
        return shipmentStatusLogVO;
    }
}
