package com.app.projectlogistics.DataTransferObject.Cargo;

import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class RQTCargoDTO {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Integer cargoId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 300)
    private String cargoName;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Digits(integer = 7, fraction = 2)
    private BigDecimal cargoWeight;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer cargoQuantity;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50)
    private String cargoType;

    private String cargoDescription;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Max(value = Integer.MAX_VALUE)
    private Integer updatedBy;

    public String getCargoDescription() {
        return cargoDescription;
    }

    public void setCargoDescription(String cargoDescription) {
        this.cargoDescription = cargoDescription;
    }

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

    public BigDecimal getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(BigDecimal cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
