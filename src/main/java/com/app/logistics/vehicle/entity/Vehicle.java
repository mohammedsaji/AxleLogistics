package com.app.logistics.vehicle.entity;

import com.app.logistics.common.entity.BaseEntity;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.operator.entity.Operator;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "TRNSP_VEHCLE_MST", indexes = {
        @Index(name = "IDX_TRNSP_VEHCLE_MST_OPRTR_ID", columnList = "OPRTR_ID")
})
public class Vehicle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name = "VEHCLE_ID")
    private Integer vehicleId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50)
    @Column(name = "VEHCLE_TYPE")
    private String vehicleType;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50)
    @Column(name = "VEHCLE_NUMBER")
    private String vehicleNumber;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JoinColumn(name = "OPRTR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator operator;

    @Positive(groups = {OnCreate.class, OnUpdate.class})
    @Max(value = Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    public Operator getOperatorVO() {
        return operator;
    }

    public void setOperatorVO(Operator operator) {
        this.operator = operator;
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

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
}