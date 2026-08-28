package com.app.logistics.shipmentStatusLog.entity;

import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.vehicle.entity.Vehicle;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.shipmentStatus.entity.ShipmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name= "SHIPPNG_STATUS_INFO_LOGS", indexes={
        @Index(name= "IDX_SHIPPNG_STATUS_INFO_LOGS_SHIPPNG_LOG_SHIPPNG_STATS_ID", columnList = "SHIPPNG_LOG_SHIPPNG_STATS_ID"),
        @Index(name= "IDX_SHIPPNG_STATUS_INFO_LOGS_SHIPPNG_LOG_CRGO_ID", columnList = "SHIPPNG_LOG_CRGO_ID"),
        @Index(name = "IDX_SHIPPNG_STATUS_INFO_LOGS_SHIPPNG_LOG_OPRTR_ID", columnList = "SHIPPNG_LOG_OPRTR_ID"),
        @Index(name = "IDX_SHIPPNG_STATUS_INFO_LOGS_SHIPPNG_LOG_DRVR_ID", columnList = "SHIPPNG_LOG_DRVR_ID"),
        @Index(name= "IDX_SHIPPNG_STATUS_INFO_LOGS_SHIPPNG_LOG_VEHCLE_ID", columnList="SHIPPNG_LOG_VEHCLE_ID")
})
public class ShipmentStatusLog {

    @Id
    @GeneratedValue( strategy= GenerationType.SEQUENCE, generator = "shipping-status-info-log-seq-gen")
    @SequenceGenerator(name="shipping-status-info-log-seq-gen",
            sequenceName ="SHPPNG_STATUS_INFO_LOGS_SHIP_ID_SEQ",
            initialValue = 1,
            allocationSize = 50)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name="SHIPPNG_LOG_STATS_ID")
    private Integer shippingStatusLogId;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SHIPPNG_LOG_SHIPPNG_STATS_ID")
    private ShipmentStatus shipmentStatus;

    public ShipmentStatus getShipmentStatusVO() {
        return shipmentStatus;
    }

    public void setShipmentStatusVO(ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    @JoinColumn(name="SHIPPNG_LOG_CRGO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private Cargo cargo;

    public Cargo getShippingCargoVO() {
        return cargo;
    }

    public void setShippingCargoVO(Cargo cargo) {
        this.cargo = cargo;
    }

    @JoinColumn(name="SHIPPNG_LOG_OPRTR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private Operator operator;

    public Operator getShippingOperatorVO() {
        return operator;
    }

    public void setShippingOperatorVO(Operator operator) {
        this.operator = operator;
    }

    @JoinColumn(name="SHIPPNG_LOG_DRVR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private Driver driver;

    public Driver getShippingDriverVO() {
        return driver;
    }

    public void setShippingDriverVO(Driver driver) {
        this.driver = driver;
    }

    @JoinColumn(name="SHIPPNG_LOG_VEHCLE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private Vehicle vehicleVO;

    public Vehicle getShippingVehicleVO() {
        return vehicleVO;
    }

    public void setShippingVehicleVO(Vehicle vehicleVO) {
        this.vehicleVO = vehicleVO;
    }

    @Column(name = "CURRNT_LOC")
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(min=10,max=50)
    private String currentLocation;

    @Column(name="SHIPPNG_STATS")
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "SHIPPED|ARRIVED|IN-TRANSIT|DELAYED|OUT-FOR-DELIVERY|DELIVERED", groups = {OnCreate.class, OnUpdate.class})
    private String shippingStatus;

    @Column(name="UPDATED_AT")
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private LocalDateTime updatedAt;

    @Column(name="UPDATED_BY")
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
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

    public Integer getShippingStatusLogId() {
        return shippingStatusLogId;
    }

    public void setShippingStatusLogId(Integer shippingStatusLogId) {
        this.shippingStatusLogId = shippingStatusLogId;
    }
}