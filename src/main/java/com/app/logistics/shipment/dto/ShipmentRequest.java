package com.app.logistics.shipment.dto;

import com.app.logistics.common.validations.OnShipmentSave;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.shipmentStatusLog.utils.OnDependentSave;
import com.app.logistics.shipmentStatusLog.utils.OnOperatorSave;
import com.app.logistics.shipmentStatusLog.utils.OnStatusSave;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShipmentRequest {

    @Null(groups = OnShipmentSave.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer shippingId;

    @NotBlank(groups = OnShipmentSave.class)
    @Size(min=10, max = 250)
    private String shippingFrom;

    @NotNull(groups = OnShipmentSave.class)
    private BigDecimal originLatitude;

    @NotNull(groups = OnShipmentSave.class)
    private BigDecimal originLongitude;

    @NotBlank(groups = {OnStatusSave.class, OnOperatorSave.class, OnDependentSave.class})
    @Size(min=10, max=250)
    private String currentLocation;

    @NotNull(groups = OnShipmentSave.class)
    private BigDecimal currentLatitude;

    @NotNull(groups = OnShipmentSave.class)
    private BigDecimal currentLongitude;

    @NotBlank(groups = OnShipmentSave.class)
    @Size(min=10, max = 250)
    private String shippingTo;

    @NotNull(groups = OnShipmentSave.class)
    private BigDecimal destinationLatitude;

    @NotNull(groups = OnShipmentSave.class)
    private BigDecimal destinationLongitude;

    @Null(groups = OnShipmentSave.class)
    private Integer customerId;

    @Null(groups = OnShipmentSave.class)
    private Integer cargoId;

    @NotNull(groups = OnShipmentSave.class)
    private LocalDateTime deliveryDate;

    public Integer getShippingId() {
        return shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCargoId() {
        return cargoId;
    }

    public void setCargoId(Integer cargoId) {
        this.cargoId = cargoId;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public BigDecimal getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(BigDecimal destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public BigDecimal getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(BigDecimal destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public BigDecimal getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(BigDecimal originLatitude) {
        this.originLatitude = originLatitude;
    }

    public BigDecimal getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(BigDecimal originLongitude) {
        this.originLongitude = originLongitude;
    }

    public BigDecimal getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(BigDecimal currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public BigDecimal getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(BigDecimal currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getShippingFrom() {
        return shippingFrom;
    }

    public void setShippingFrom(String shippingFrom) {
        this.shippingFrom = shippingFrom;
    }

    public String getShippingTo() {
        return shippingTo;
    }

    public void setShippingTo(String shippingTo) {
        this.shippingTo = shippingTo;
    }
}