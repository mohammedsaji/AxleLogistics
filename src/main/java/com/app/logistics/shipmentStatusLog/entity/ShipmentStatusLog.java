    package com.app.logistics.shipmentStatusLog.entity;

    import com.app.logistics.common.entity.BaseEntity;
    import com.app.logistics.common.validations.OnCreate;
    import com.app.logistics.common.validations.OnUpdate;
    import com.app.logistics.driver.entity.Driver;
    import com.app.logistics.shipment.entity.Shipment;
    import com.app.logistics.shipmentStatusLog.utils.OnDependentSave;
    import com.app.logistics.shipmentStatusLog.utils.OnOperatorSave;
    import com.app.logistics.shipmentStatusLog.utils.OnStatusSave;
    import com.app.logistics.vehicle.entity.Vehicle;
    import com.app.logistics.operator.entity.Operator;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.*;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;

    @Entity
    @Table(name= "SHIPPNG_STATUS_INFO_LOGS", indexes={
            @Index(name= "IDX_SHIPPNG_STATUS_LOGS_OPRTR_ID", columnList = "SHIPPNG_OPRTR_ID"),
            @Index(name= "IDX_SHIPPNG_STATUS_LOGS_DRVR_ID", columnList = "SHIPPNG_DRVR_ID"),
            @Index(name= "IDX_SHIPPNG_STATUS_LOGS_VEHCLE_ID", columnList="SHIPPNG_VEHCLE_ID"),
            @Index(name= "IDX_SHIPPNG_STATUS_LOGS_ASSIGNED_BY", columnList = "ASSIGNED_BY")
    })
    public class ShipmentStatusLog{

        @Id
        @GeneratedValue( strategy= GenerationType.SEQUENCE, generator = "shipping-status-log-seq-gen")
        @SequenceGenerator(name="shipping-status-log-seq-gen",
                sequenceName ="SHPPNG_STATUS_LOGS_ID_SEQ",
                initialValue = 1,
                allocationSize = 50)
        @Null(groups = OnCreate.class)
        @NotNull(groups = OnUpdate.class)
        @Column(name="SHIPPNG_LOG_ID")
        private Integer shippingStatusLogId;

        @NotNull(groups = {OnStatusSave.class, OnOperatorSave.class, OnDependentSave.class})
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="SHIPPNG_ID")
        private Shipment shipment;

        public Shipment getShipment() {
            return shipment;
        }

        public void setShipment(Shipment shipment) {
            this.shipment = shipment;
        }

        @JoinColumn(name="SHIPPNG_OPRTR_ID")
        @ManyToOne(fetch = FetchType.LAZY)
        @NotNull(groups = {OnStatusSave.class, OnOperatorSave.class})
        private Operator operator;

        @JoinColumn(name="SHIPPNG_DRVR_ID")
        @ManyToOne(fetch = FetchType.LAZY)
        @NotNull(groups = {OnStatusSave.class, OnDependentSave.class})
        private Driver driver;

        @JoinColumn(name="SHIPPNG_VEHCLE_ID")
        @ManyToOne(fetch = FetchType.LAZY)
        @NotNull(groups = {OnStatusSave.class, OnDependentSave.class})
        private Vehicle vehicle;

        @Column(name = "CURRNT_LOC")
        @NotBlank(groups = {OnStatusSave.class, OnOperatorSave.class, OnDependentSave.class})
        @Size(min=10,max=100)
        private String currentLocation;

        @Column(name = "CURRNT_LATITUDE", precision = 10, scale = 7)
        private BigDecimal currentLatitude;

        @Column(name = "CURRNT_LONGITUDE", precision = 10, scale = 7)
        private BigDecimal currentLongitude;

        @Column(name="SHIPPNG_STATS")
        @NotBlank(groups = {OnDependentSave.class, OnStatusSave.class})
        @Pattern(regexp = "SHIPPED|ARRIVED|IN-TRANSIT|DELAYED|OUT-FOR-DELIVERY|DELIVERED", groups = {OnCreate.class, OnUpdate.class})
        private String shippingStatus;

        @Column(name="UPDATED_AT")
        @NotNull(groups = {OnStatusSave.class, OnOperatorSave.class, OnDependentSave.class})
        private LocalDateTime updatedAt;

        @Column(name="UPDATED_BY")
        @NotNull(groups = {OnStatusSave.class, OnOperatorSave.class, OnDependentSave.class})
        private Integer updatedBy;

        @Column(name="ASSIGNED_BY")
        @NotNull(groups = {OnDependentSave.class})
        private Integer assignedBy;

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

        public Integer getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(Integer updatedBy) {
            this.updatedBy = updatedBy;
        }

        public Integer getShippingStatusLogId() {
            return shippingStatusLogId;
        }

        public void setShippingStatusLogId(Integer shippingStatusLogId) {
            this.shippingStatusLogId = shippingStatusLogId;
        }

        public Integer getAssignedBy() {
            return assignedBy;
        }

        public void setAssignedBy(Integer assignedBy) {
            this.assignedBy = assignedBy;
        }

        public BigDecimal getCurrentLatitude() {
            return currentLatitude;
        }

        public void setCurrentLatitude(BigDecimal currentLatitude) {
            this.currentLatitude = currentLatitude;
        }

        public BigDecimal getCurrentLongitude() {
            return currentLongitude;
        }

        public void setCurrentLongitude(BigDecimal currentLongitude) {
            this.currentLongitude = currentLongitude;
        }

        public String getCurrentLocation() {
            return currentLocation;
        }

        public void setCurrentLocation(String currentLocation) {
            this.currentLocation = currentLocation;
        }

        public Operator getOperator() {
            return operator;
        }

        public void setOperator(Operator operator) {
            this.operator = operator;
        }

        public Driver getDriver() {
            return driver;
        }

        public void setDriver(Driver driver) {
            this.driver = driver;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public void setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
        }
    }