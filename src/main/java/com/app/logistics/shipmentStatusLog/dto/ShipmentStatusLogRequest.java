package com.app.logistics.shipmentStatusLog.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ShipmentStatusLogRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer shippingStatusLogId;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer shippingStatusId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 10, max = 50, groups = {OnCreate.class, OnUpdate.class})
    private String currentLocation;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer cargoId;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer operatorId;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer driverId;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer vehicleId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(
            regexp = "SHIPPED|ARRIVED|IN-TRANSIT|DELAYED|OUT-FOR-DELIVERY|DELIVERED",
            message = "Invalid shipping status",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String shippingStatus;

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
}