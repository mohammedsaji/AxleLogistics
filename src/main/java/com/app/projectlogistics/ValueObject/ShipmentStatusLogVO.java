package com.app.projectlogistics.ValueObject;

import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
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
public class ShipmentStatusLogVO {

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
    private ShipmentStatusVO shipmentStatusVO;

    public ShipmentStatusVO getShipmentStatusVO() {
        return shipmentStatusVO;
    }

    public void setShipmentStatusVO(ShipmentStatusVO shipmentStatusVO) {
        this.shipmentStatusVO = shipmentStatusVO;
    }

    @JoinColumn(name="SHIPPNG_LOG_CRGO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private CargoVO cargoVO;

    public CargoVO getShippingCargoVO() {
        return cargoVO;
    }

    public void setShippingCargoVO(CargoVO cargoVO) {
        this.cargoVO = cargoVO;
    }

    @JoinColumn(name="SHIPPNG_LOG_OPRTR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private OperatorVO operatorVO;

    public OperatorVO getShippingOperatorVO() {
        return operatorVO;
    }

    public void setShippingOperatorVO(OperatorVO operatorVO) {
        this.operatorVO = operatorVO;
    }

    @JoinColumn(name="SHIPPNG_LOG_DRVR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private DriverVO driverVO;

    public DriverVO getShippingDriverVO() {
        return driverVO;
    }

    public void setShippingDriverVO(DriverVO driverVO) {
        this.driverVO = driverVO;
    }

    @JoinColumn(name="SHIPPNG_LOG_VEHCLE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private VehicleVO vehicleVO;

    public VehicleVO getShippingVehicleVO() {
        return vehicleVO;
    }

    public void setShippingVehicleVO(VehicleVO vehicleVO) {
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