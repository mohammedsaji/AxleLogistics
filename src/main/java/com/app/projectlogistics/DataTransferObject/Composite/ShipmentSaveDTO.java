package com.app.projectlogistics.DataTransferObject.Composite;

import com.app.projectlogistics.DataTransferObject.Cargo.RQTCargoDTO;
import com.app.projectlogistics.DataTransferObject.Customer.RQTCustomerDTO;
import com.app.projectlogistics.DataTransferObject.Driver.RQTDriverDTO;
import com.app.projectlogistics.DataTransferObject.Operator.RQTOperatorDTO;
import com.app.projectlogistics.DataTransferObject.Shipment.RQTShipmentDTO;
import com.app.projectlogistics.DataTransferObject.ShipmentStatus.RQTShipmentStatusDTO;
import com.app.projectlogistics.DataTransferObject.Vehicle.RQTVehicleDTO;
import org.springframework.stereotype.Component;

@Component
public class ShipmentSaveDTO {

    private RQTCustomerDTO rqtCustomerDTO;

    private RQTCargoDTO rqtCargoDTO;

    private RQTShipmentDTO rqtShipmentDTO;

    private RQTShipmentStatusDTO rqtShipmentStatusDTO;

    private RQTOperatorDTO rqtOperatorDTO;

    private RQTDriverDTO rqtDriverDTO;

    private RQTVehicleDTO rqtVehicleDTO;


    public RQTCargoDTO getRqtCargoDTO() {
        return rqtCargoDTO;
    }

    public void setRqtCargoDTO(RQTCargoDTO rqtCargoDTO) {
        this.rqtCargoDTO = rqtCargoDTO;
    }

    public RQTCustomerDTO getRqtCustomerDTO() {
        return rqtCustomerDTO;
    }

    public void setRqtCustomerDTO(RQTCustomerDTO rqtCustomerDTO) {
        this.rqtCustomerDTO = rqtCustomerDTO;
    }

    public RQTDriverDTO getRqtDriverDTO() {
        return rqtDriverDTO;
    }

    public void setRqtDriverDTO(RQTDriverDTO rqtDriverDTO) {
        this.rqtDriverDTO = rqtDriverDTO;
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
