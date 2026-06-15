package com.app.projectlogistics.DataTransferObject.ShipmentStatusLogDTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class RSPShipmentStatusLogDTO {

    private Integer shippingStatusLogId;

    private Integer shippingStatusId;

    @Size(min=10)
    @Size(max=50)
    private String currentLocation;

    private Integer cargoId;

    private Integer OperatorId;

    private Integer driverId;

    private Integer vehicleId;

    @Pattern(regexp = "SHIPPED|ARRIVED|IN-TRANSIT|DELAYED|OUT-FOR-DELIVERY|DELIVERED")
    private String shippingStatus;

    private LocalDateTime updatedAt;

    private Integer updatedby;

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Integer getShippingStatusLogId() {
        return shippingStatusLogId;
    }

    public void setShippingStatusLogId(Integer shippingStatusLogId) {
        this.shippingStatusLogId = shippingStatusLogId;
    }

    public Integer getCargoId() {
        return cargoId;
    }

    public void setCargoId(Integer cargoId) {
        this.cargoId = cargoId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getOperatorId() {
        return OperatorId;
    }

    public void setOperatorId(Integer operatorId) {
        OperatorId = operatorId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public Integer getShippingStatusId() {
        return shippingStatusId;
    }

    public void setShippingStatusId(Integer shippingStatusId) {
        this.shippingStatusId = shippingStatusId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(Integer updatedby) {
        this.updatedby = updatedby;
    }
}
