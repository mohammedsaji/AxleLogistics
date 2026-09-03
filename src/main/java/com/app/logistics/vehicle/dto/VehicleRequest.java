package com.app.logistics.vehicle.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class VehicleRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer vehicleId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50, groups = {OnCreate.class, OnUpdate.class})
    private String vehicleType;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50, groups = {OnCreate.class, OnUpdate.class})
    private String vehicleNumber;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    private Integer operatorId;

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
}