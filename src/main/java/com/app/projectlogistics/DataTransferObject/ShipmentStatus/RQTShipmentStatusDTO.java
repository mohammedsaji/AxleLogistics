package com.app.projectlogistics.DataTransferObject.ShipmentStatus;
import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class RQTShipmentStatusDTO {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Integer shippingStatusId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 10, max = 50)
    private String currentLocation;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Integer cargoId;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Integer operatorId;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Integer driverId;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Integer vehicleId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "SHIPPED|IN-TRANSIT|DELAYED|ARRIVED|OUT-FOR-DELIVERY|DELIVERED")
    private String shippingStatus;

    private LocalDateTime updatedAt;

    @Max(value = Integer.MAX_VALUE)
    private Integer updatedby;

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

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
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
