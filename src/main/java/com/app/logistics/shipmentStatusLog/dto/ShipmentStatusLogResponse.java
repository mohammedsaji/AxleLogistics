package com.app.logistics.shipmentStatusLog.dto;

import java.time.LocalDateTime;

public class ShipmentStatusLogResponse {

    private Integer shippingStatusLogId;
    private Integer shippingStatusId;
    private String currentLocation;
    private Integer cargoId;
    private Integer operatorId;
    private Integer driverId;
    private Integer vehicleId;
    private String shippingStatus;
    private LocalDateTime updatedAt;
    private Integer updatedBy;

    public Integer getShippingStatusLogId() {
        return shippingStatusLogId;
    }

    public void setShippingStatusLogId(Integer shippingStatusLogId) {
        this.shippingStatusLogId = shippingStatusLogId;
    }

    public Integer getShippingStatusId() {
        return shippingStatusId;
    }

    public void setShippingStatusId(Integer shippingStatusId) {
        this.shippingStatusId = shippingStatusId;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Integer getCargoId() {
        return cargoId;
    }

    public void setCargoId(Integer cargoId) {
        this.cargoId = cargoId;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
}