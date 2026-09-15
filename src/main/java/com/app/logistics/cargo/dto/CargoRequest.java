package com.app.logistics.cargo.dto;

import com.app.logistics.common.validations.OnShipmentSave;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CargoRequest {

    @Null(groups = OnShipmentSave.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer cargoId;

    @NotBlank(groups = {OnShipmentSave.class, OnUpdate.class})
    @Size(max = 300, groups = {OnShipmentSave.class, OnUpdate.class})
    private String cargoName;

    @NotNull(groups = {OnShipmentSave.class, OnUpdate.class})
    @Positive(groups = {OnShipmentSave.class, OnUpdate.class})
    @Digits(
            integer = 7,
            fraction = 2,
            groups = {OnShipmentSave.class, OnUpdate.class}
    )
    private BigDecimal cargoWeight;

    @NotNull(groups = {OnShipmentSave.class, OnUpdate.class})
    @Min(
            value = 1,
            message = "Quantity must be at least 1",
            groups = {OnShipmentSave.class, OnUpdate.class}
    )
    private Integer cargoQuantity;

    @NotBlank(groups = {OnShipmentSave.class, OnUpdate.class})
    @Size(max = 50, groups = {OnShipmentSave.class, OnUpdate.class})
    private String cargoType;

    @Size(max = 1000, groups = {OnShipmentSave.class, OnUpdate.class})
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