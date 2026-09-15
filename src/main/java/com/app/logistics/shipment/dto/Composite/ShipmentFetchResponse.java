package com.app.logistics.shipment.dto.Composite;

import com.app.logistics.cargo.dto.CargoResponse;
import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.customer.dto.CustomerResponse;
import com.app.logistics.shipment.dto.ShipmentResponse;
import com.app.logistics.shipment.entity.Shipment;

public class ShipmentFetchResponse {

    private CustomerResponse customerResponse;

    private CargoResponse cargoResponse;

    private ShipmentResponse shipmentResponse;

    public CustomerResponse getCustomerResponse() {
        return customerResponse;
    }

    public void setCustomerResponse(CustomerResponse customerResponse) {
        this.customerResponse = customerResponse;
    }

    public CargoResponse getCargoResponse() {
        return cargoResponse;
    }

    public void setCargoResponse(CargoResponse cargoResponse) {
        this.cargoResponse = cargoResponse;
    }

    public ShipmentResponse getShipmentResponse() {
        return shipmentResponse;
    }

    public void setShipmentResponse(ShipmentResponse shipmentResponse) {
        this.shipmentResponse = shipmentResponse;
    }
}
