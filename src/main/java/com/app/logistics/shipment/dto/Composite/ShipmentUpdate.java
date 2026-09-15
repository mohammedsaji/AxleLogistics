package com.app.logistics.shipment.dto.Composite;

import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.shipment.dto.ShipmentRequest;
import com.app.logistics.shipmentStatusLog.dto.ShipmentStatusLogRequest;
import com.app.logistics.shipmentStatusLog.utils.OnOperatorSave;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;

public class ShipmentUpdate {

    @Valid
    @ConvertGroup(from = OnUpdate.class, to = OnUpdate.class)
    private ShipmentRequest shipmentRequest;

    @Valid
    @ConvertGroup(from = OnUpdate.class, to = OnOperatorSave.class)
    private ShipmentStatusLogRequest shipmentStatusLogRequest;

    public ShipmentRequest getShipmentRequest() {
        return shipmentRequest;
    }

    public void setShipmentRequest(ShipmentRequest shipmentRequest) {
        this.shipmentRequest = shipmentRequest;
    }

    public ShipmentStatusLogRequest getShipmentStatusLogRequest() {
        return shipmentStatusLogRequest;
    }

    public void setShipmentStatusLogRequest(ShipmentStatusLogRequest shipmentStatusLogRequest) {
        this.shipmentStatusLogRequest = shipmentStatusLogRequest;
    }
}
