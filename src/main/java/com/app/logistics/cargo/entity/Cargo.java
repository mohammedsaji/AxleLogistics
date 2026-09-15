package com.app.logistics.cargo.entity;

import com.app.logistics.common.validations.OnShipmentSave;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CRGO_INFO")
public class Cargo{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipping-cargo-info-seq-gen")
    @SequenceGenerator(name = "shipping-cargo-info-seq-gen",
            sequenceName = "CRGO_INFO_CRGO_ID_SEQ",
            initialValue = 1,
            allocationSize = 50)
    @Null(groups = OnShipmentSave.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name = "CRGO_ID")
    private Integer cargoId;

    @NotBlank(groups = {OnShipmentSave.class, OnUpdate.class})
    @Size(max = 300)
    @Column(name = "CRGO_NAME")
    private String cargoName;

    @NotNull(groups = {OnShipmentSave.class, OnUpdate.class})
    @Digits(integer = 7, fraction = 2)
    @Column(name = "CRGO_WEIGT")
    private BigDecimal cargoWeight;

    @NotNull(groups = {OnShipmentSave.class, OnUpdate.class})
    @Positive(groups = {OnShipmentSave.class, OnUpdate.class})
    @Max(value = Integer.MAX_VALUE, groups = {OnShipmentSave.class, OnUpdate.class})
    @Column(name = "CRGO_QNTITY")
    private Integer cargoQuantity;

    @NotBlank(groups = {OnShipmentSave.class, OnUpdate.class})
    @Size(max = 50)
    @Column(name = "CRGO_TYPE")
    private String cargoType;

    @NotBlank(groups = {OnShipmentSave.class, OnUpdate.class})
    @Size(max = 255)
    @Column(name = "CRGO_DESC")
    private String cargoDescription;

    @Positive(groups = {OnShipmentSave.class, OnUpdate.class})
    @Max(value = Integer.MAX_VALUE, groups = {OnShipmentSave.class, OnUpdate.class})
    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

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
}