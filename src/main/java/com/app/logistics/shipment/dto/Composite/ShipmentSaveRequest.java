package com.app.logistics.shipment.dto.Composite;

import com.app.logistics.cargo.dto.CargoRequest;
import com.app.logistics.customer.dto.CustomerRequest;
import com.app.logistics.driver.dto.DriverRequest;
import com.app.logistics.operator.dto.OperatorRequest;
import com.app.logistics.shipment.dto.ShipmentRequest;
import com.app.logistics.shipmentStatus.dto.ShipmentStatusRequest;
import com.app.logistics.vehicle.dto.VehicleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class ShipmentSaveRequest {

    @Valid
    @NotNull
    private CustomerRequest customerRequest;

    @Valid
    @NotNull
    private CargoRequest cargoRequest;

    @Valid
    @NotNull
    private ShipmentRequest shipmentRequest;

    @Valid
    @NotNull
    private ShipmentStatusRequest shipmentStatusRequest;

    @Valid
    @NotNull
    private OperatorRequest operatorRequest;

    @Valid
    @NotNull
    private DriverRequest driverRequest;

    @Valid
    @NotNull
    private VehicleRequest vehicleRequest;

    public CustomerRequest getCustomerRequest() {
        return customerRequest;
    }

    public void setCustomerRequest(CustomerRequest customerRequest) {
        this.customerRequest = customerRequest;
    }

    public CargoRequest getCargoRequest() {
        return cargoRequest;
    }

    public void setCargoRequest(CargoRequest cargoRequest) {
        this.cargoRequest = cargoRequest;
    }

    public ShipmentRequest getShipmentRequest() {
        return shipmentRequest;
    }

    public void setShipmentRequest(ShipmentRequest shipmentRequest) {
        this.shipmentRequest = shipmentRequest;
    }

    public ShipmentStatusRequest getShipmentStatusRequest() {
        return shipmentStatusRequest;
    }

    public void setShipmentStatusRequest(ShipmentStatusRequest shipmentStatusRequest) {
        this.shipmentStatusRequest = shipmentStatusRequest;
    }

    public OperatorRequest getOperatorRequest() {
        return operatorRequest;
    }

    public void setOperatorRequest(OperatorRequest operatorRequest) {
        this.operatorRequest = operatorRequest;
    }

    public DriverRequest getDriverRequest() {
        return driverRequest;
    }

    public void setDriverRequest(DriverRequest driverRequest) {
        this.driverRequest = driverRequest;
    }

    public VehicleRequest getVehicleRequest() {
        return vehicleRequest;
    }

    public void setVehicleRequest(VehicleRequest vehicleRequest) {
        this.vehicleRequest = vehicleRequest;
    }
}