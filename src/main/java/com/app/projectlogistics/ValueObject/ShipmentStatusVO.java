package com.app.projectlogistics.ValueObject;

import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table( name="SHIPPNG_STATUS_INFO", indexes={
        @Index(name = "SHIPPNG_STATUS_INFO_SHIPPNG_CURRNT_OPRTR_ID_FKEY", columnList = "SHIPPNG_CURRNT_OPRTR_ID"),
        @Index(name = "SHIPPNG_STATUS_INFO_SHIPPNG_CURRNT_DRVR_ID_FKEY", columnList = "SHIPPNG_CURRNT_DRVR_ID"),
        @Index(name="SHIPPNG_STATUS_INFO_SHIPPNG_CURRNT_VEHCLE_ID_FKEY", columnList="SHIPPNG_CURRNT_VEHCLE_ID")
})
public class ShipmentStatusVO {

    @Id
    @GeneratedValue( strategy= GenerationType.SEQUENCE, generator = "shipping-status-info-seq-gen")
    @SequenceGenerator(name="shipping-status-info-seq-gen",
            sequenceName ="SHPPNG_STATUS_INFO_SHIP_ID_SEQ",
            initialValue = 1,
            allocationSize = 50)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name="SHIPPNG_STATS_ID")
    private Integer shippingStatusId;

    @JoinColumn(name="SHIPPNG_CURRNT_OPRTR_ID")
    @ManyToOne( fetch = FetchType.LAZY)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private OperatorVO operatorVO;

    public OperatorVO getShippingOperatorVO() {
        return operatorVO;
    }

    public void setShippingOperatorVO(OperatorVO operatorVO) {
        this.operatorVO = operatorVO;
    }

    @JoinColumn(name="SHIPPNG_CURRNT_DRVR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private DriverVO driverVO;

    public DriverVO getShippingDriverVO() {
        return driverVO;
    }

    public void setShippingDriverVO(DriverVO driverVO) {
        this.driverVO = driverVO;
    }

    @JoinColumn(name="SHIPPNG_CURRNT_VEHCLE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private VehicleVO currentVehicleVO;

    public VehicleVO getShippingVehicleVO() {
        return currentVehicleVO;
    }

    public void setShippingVehicleVO(VehicleVO currentVehicleVO) {
        this.currentVehicleVO = currentVehicleVO;
    }

    @Column(name = "CURRNT_LOC")
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(min=10,max=255)
    private String currentLocation;

    @Column(name="SHIPPNG_STATS")
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "SHIPPED|ARRIVED|IN-TRANSIT|DELAYED|OUT-FOR-DELIVERY|DELIVERED", groups = {OnCreate.class, OnUpdate.class})
    private String shippingStatus;

    @Column(name="UPDATED_AT")
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private LocalDateTime updatedAt;

    @Column(name="UPDATED_BY")
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    @Max(value = Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    private Integer updatedby;


    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public Integer getShippingStatusId() {
        return shippingStatusId;
    }

    public void setShippingStatusId(Integer shippingStatusId) {
        this.shippingStatusId = shippingStatusId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(Integer updatedby) {
        this.updatedby = updatedby;
    }
}