package com.app.logistics.cargo.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CargoRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer cargoId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 300, groups = {OnCreate.class, OnUpdate.class})
    private String cargoName;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    @Digits(
            integer = 7,
            fraction = 2,
            groups = {OnCreate.class, OnUpdate.class}
    )
    private BigDecimal cargoWeight;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Min(
            value = 1,
            message = "Quantity must be at least 1",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Integer cargoQuantity;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50, groups = {OnCreate.class, OnUpdate.class})
    private String cargoType;

    @Size(max = 1000, groups = {OnCreate.class, OnUpdate.class})
    private String cargoDescription;

    public Integer getCargoId() {
        return cargoId;
    }

    public void setCargoId(Integer cargoId) {
        this.cargoId = cargoId;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public BigDecimal getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(BigDecimal cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public Integer getCargoQuantity() {
        return cargoQuantity;
    }

    public void setCargoQuantity(Integer cargoQuantity) {
        this.cargoQuantity = cargoQuantity;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public String getCargoDescription() {
        return cargoDescription;
    }

    public void setCargoDescription(String cargoDescription) {
        this.cargoDescription = cargoDescription;
    }
}