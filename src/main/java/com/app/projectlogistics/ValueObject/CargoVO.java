package com.app.projectlogistics.ValueObject;

import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="CRGO_INFO")
public class CargoVO {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE , generator="shipping-cargo-info-seq-gen")
    @SequenceGenerator(name="shipping-cargo-info-seq-gen",
            sequenceName = "CRGO_INFO_CRGO_ID_SEQ",
            initialValue=1,
            allocationSize=50)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name="CRGO_ID")
    private Integer cargoId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=300)
    @Column(name="CRGO_NAME")
    private String cargoName;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Digits(integer=7,fraction=2)
    @Column(name="CRGO_WEIGT")
    private BigDecimal cargoWeight;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    @Max(value = Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    @Column(name="CRGO_QNTITY")
    private Integer cargoQuantity;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=50)
    @Column(name="CRGO_TYPE")
    private String cargoType;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=255)
    @Column(name="CRGO_DESC")
    private String cargoDescription;

    @Column(name="CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name="UPDATED_AT")
    private LocalDateTime updatedAt;

    @Positive(groups = {OnCreate.class, OnUpdate.class})
    @Max(value=Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    @Column(name="UPDATED_BY")
    private Integer updatedBy;

    // --- Original methods preserved ---

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