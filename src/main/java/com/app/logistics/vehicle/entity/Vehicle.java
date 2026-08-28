package com.app.logistics.vehicle.entity;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.operator.entity.Operator;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name="TRNSP_VEHCLE_MST", indexes={
        @Index(name = "IDX_TRNSP_VEHCLE_MST_OPRTR_ID", columnList="OPRTR_ID")
})
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name="VEHCLE_ID")
    private Integer vehicleId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=50)
    @Column(name="VEHCLE_TYPE")
    private String vehicleType;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=50)
    @Column(name="VEHCLE_NUMBER")
    private String vehicleNumber;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JoinColumn(name="OPRTR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator operator;

    public Operator getshippingOperatorVO() {
        return operator;
    }

    public void setshippingOperatorVO(Operator operator) {
        this.operator = operator;
    }

    @Column(name="CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name="UPDATED_AT")
    private LocalDateTime updatedAt;

    @Positive(groups = {OnCreate.class, OnUpdate.class})
    @Max(value = Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    @Column(name="UPDATED_BY")
    private Integer updatedBy;

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

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}