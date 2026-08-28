package com.app.logistics.shipment.dto.Composite;

import com.app.logistics.cargo.dto.CargoRequest;
import com.app.logistics.customer.dto.CustomerRequest;
import com.app.logistics.driver.dto.DriverRequest;
import com.app.logistics.dto.Operator.RQTOperatorDTO;
import com.app.logistics.dto.Shipment.RQTShipmentDTO;
import com.app.logistics.dto.ShipmentStatus.RQTShipmentStatusDTO;
import com.app.logistics.dto.Vehicle.RQTVehicleDTO;
import org.springframework.stereotype.Component;

@Component
public class ShipmentSaveRequest {

    private CustomerRequest customerRequest;

    private CargoRequest cargoRequest;

    private RQTShipmentDTO rqtShipmentDTO;

    private RQTShipmentStatusDTO rqtShipmentStatusDTO;

    private RQTOperatorDTO rqtOperatorDTO;

    private DriverRequest driverRequest;

    private RQTVehicleDTO rqtVehicleDTO;


    public CargoRequest getRqtCargoDTO() {
        return cargoRequest;
    }

    public void setRqtCargoDTO(CargoRequest cargoRequest) {
        this.cargoRequest = cargoRequest;
    }

    public CustomerRequest getRqtCustomerDTO() {
        return customerRequest;
    }

    public void setRqtCustomerDTO(CustomerRequest customerRequest) {
        this.customerRequest = customerRequest;
    }

    public DriverRequest getRqtDriverDTO() {
        return driverRequest;
    }

    public void setRqtDriverDTO(DriverRequest driverRequest) {
        this.driverRequest = driverRequest;
    }

    public RQTOperatorDTO getRqtOperatorDTO() {
        return rqtOperatorDTO;
    }

    public void setRqtOperatorDTO(RQTOperatorDTO rqtOperatorDTO) {
        this.rqtOperatorDTO = rqtOperatorDTO;
    }

    public RQTShipmentDTO getRqtShipmentDTO() {
        return rqtShipmentDTO;
    }

    public void setRqtShipmentDTO(RQTShipmentDTO rqtShipmentDTO) {
        this.rqtShipmentDTO = rqtShipmentDTO;
    }

    public RQTShipmentStatusDTO getRqtShipmentStatusDTO() {
        return rqtShipmentStatusDTO;
    }

    public void setRqtShipmentStatusDTO(RQTShipmentStatusDTO rqtShipmentStatusDTO) {
        this.rqtShipmentStatusDTO = rqtShipmentStatusDTO;
    }

    public RQTVehicleDTO getRqtVehicleDTO() {
        return rqtVehicleDTO;
    }

    public void setRqtVehicleDTO(RQTVehicleDTO rqtVehicleDTO) {
        this.rqtVehicleDTO = rqtVehicleDTO;
    }
}
