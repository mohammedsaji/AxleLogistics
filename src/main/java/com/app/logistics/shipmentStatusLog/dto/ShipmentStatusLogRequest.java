package com.app.logistics.shipmentStatusLog.dto;

import com.app.logistics.common.validations.OnRead;
import com.app.logistics.shipmentStatusLog.utils.OnDependentSave;
import com.app.logistics.shipmentStatusLog.utils.OnOperatorSave;
import com.app.logistics.shipmentStatusLog.utils.OnStatusSave;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShipmentStatusLogRequest {

    @NotNull(groups = OnRead.class)
    private Integer shippingStatusLogId;

    @NotNull(groups = { OnOperatorSave.class, OnDependentSave.class, OnStatusSave.class})
    @Positive(groups = { OnOperatorSave.class, OnDependentSave.class, OnStatusSave.class})
    private Integer shippingId;

    @NotBlank(groups = { OnOperatorSave.class, OnDependentSave.class, OnStatusSave.class})
    @Size(min = 10, max = 100, groups = { OnOperatorSave.class, OnDependentSave.class, OnStatusSave.class})
    private String currentLocation;

    @NotNull(groups = { OnOperatorSave.class, OnDependentSave.class, OnStatusSave.class})
    private BigDecimal currentLatitude;

    @NotNull(groups = { OnOperatorSave.class, OnDependentSave.class, OnStatusSave.class})
    private BigDecimal currentLongitude;

    @NotNull(groups = { OnStatusSave.class})
    private BigDecimal locationAccuracy;

    @NotNull(groups = { OnDependentSave.class, OnStatusSave.class})
    private Integer operatorId;

    @NotNull(groups = { OnDependentSave.class, OnStatusSave.class})
    private Integer driverId;

    @NotNull(groups = { OnDependentSave.class, OnStatusSave.class})
    private Integer vehicleId;

    @NotBlank(groups = {OnStatusSave.class})
    @Pattern(
            regexp = "SHIPPED|ARRIVED|IN-TRANSIT|DELAYED|OUT-FOR-DELIVERY|DELIVERED",
            message = "Invalid shipping status",
            groups = { OnStatusSave.class}
    )
    private String shippingStatus;

    private LocalDateTime updatedAt;

    public Integer getShippingStatusLogId() {
        return shippingStatusLogId;
    }

    public void setShippingStatusLogId(Integer shippingStatusLogId) {
        this.shippingStatusLogId = shippingStatusLogId;
    }

    public Integer getShippingId() {
        return shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
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

    public BigDecimal getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(BigDecimal currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public BigDecimal getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(BigDecimal currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public BigDecimal getLocationAccuracy() {
        return locationAccuracy;
    }

    public void setLocationAccuracy(BigDecimal locationAccuracy) {
        this.locationAccuracy = locationAccuracy;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}