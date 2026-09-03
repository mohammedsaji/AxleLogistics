package com.app.logistics.shipment.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ShipmentRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer shippingId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50, groups = {OnCreate.class, OnUpdate.class})
    private String shippingFrom;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50, groups = {OnCreate.class, OnUpdate.class})
    private String shippingTo;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer customerId;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer cargoId;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer statusId;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private LocalDateTime deliveryDate;

    public Integer getShippingId() {
        return shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
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

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}