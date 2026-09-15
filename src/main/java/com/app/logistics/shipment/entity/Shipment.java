package com.app.logistics.shipment.entity;

import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.common.entity.BaseEntity;
import com.app.logistics.common.validations.OnShipmentSave;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.customer.entity.Customer;
import com.app.logistics.shipmentStatusLog.utils.OnDependentSave;
import com.app.logistics.shipmentStatusLog.utils.OnOperatorSave;
import com.app.logistics.shipmentStatusLog.utils.OnStatusSave;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SHIPPNG_INFO", indexes = {
        @Index( name="IDX_SHIPPNG_INFO_SHIPPNG_CRGO_ID", columnList="SHIPPNG_CRGO_ID"),
        @Index( name="IDX_SHIPPNG_INFO_CUSTMR_ID" , columnList="CUSTMR_ID")
})
public class Shipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipping-info-seq_gen")
    @SequenceGenerator(name = "shipping-info-seq_gen",
            sequenceName = "SHIP_INFO_SHIPPNG_ID_SEQ",
            initialValue = 1,
            allocationSize = 50)
    @Null(groups = OnShipmentSave.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name="SHIPPNG_ID")
    private Integer shippingId;

    @NotNull(groups = {OnShipmentSave.class, OnUpdate.class})
    @JoinColumn( name = "SHIPPNG_CRGO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cargo Cargo;

    public Cargo getShippingCargo() {
        return Cargo;
    }

    public void setShippingCargo(Cargo Cargo) {
        this.Cargo = Cargo;
    }

    @NotBlank(groups = {OnShipmentSave.class, OnUpdate.class})
    @Size(min=10,max=250)
    @Column( name = "SHIPPNG_FROM")
    private String shippingFrom;

    @Column(name = "ORGN_LATITUDE", nullable = false, precision = 10, scale = 7)
    private BigDecimal originLatitude;

    @Column(name = "ORGN_LONGITUDE", nullable = false, precision = 10, scale = 7)
    private BigDecimal originLongitude;


    @Column(name = "CURRNT_LOC")
    @NotBlank(groups = {OnStatusSave.class, OnOperatorSave.class, OnDependentSave.class})
    @Size(min=10,max=250)
    private String currentLocation;

    @Column(name = "CURRNT_LATITUDE", precision = 10, scale = 7)
    private BigDecimal currentLatitude;

    @Column(name = "CURRNT_LONGITUDE", precision = 10, scale = 7)
    private BigDecimal currentLongitude;

    @NotBlank(groups = {OnShipmentSave.class, OnUpdate.class})
    @Size(min=10,max=250)
    @Column(name = "SHIPPNG_TO")
    private String shippingTo;

    @Column(name = "DEST_LATITUDE", nullable = false, precision = 10, scale = 7)
    private BigDecimal destinationLatitude;

    @Column(name = "DEST_LONGITUDE", nullable = false, precision = 10, scale = 7)
    private BigDecimal destinationLongitude;

    @NotNull(groups = {OnShipmentSave.class, OnUpdate.class})
    @JoinColumn(name="CUSTMR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public Customer getShippingForCustomer() {
        return customer;
    }

    public void setShippingForCustomer(Customer customer) {
        this.customer = customer;
    }

    @NotNull(groups = {OnShipmentSave.class, OnUpdate.class})
    @Column(name="EXPECTED_DELIVERY_DATE")
    private LocalDateTime deliveryDate;

    @Column(name = "UPDATED_BY")
    @Positive(groups = {OnShipmentSave.class, OnUpdate.class})
    @Max(value = Integer.MAX_VALUE, groups = {OnShipmentSave.class, OnUpdate.class})
    private Integer updatedBy;

    public Integer getShippingId() {
        return shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public BigDecimal getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(BigDecimal destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public BigDecimal getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(BigDecimal destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
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

    public BigDecimal getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(BigDecimal originLatitude) {
        this.originLatitude = originLatitude;
    }

    public BigDecimal getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(BigDecimal originLongitude) {
        this.originLongitude = originLongitude;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getShippingFrom() {
        return shippingFrom;
    }

    public void setShippingFrom(String shippingFrom) {
        this.shippingFrom = shippingFrom;
    }

    public String getShippingTo() {
        return shippingTo;
    }

    public void setShippingTo(String shippingTo) {
        this.shippingTo = shippingTo;
    }
}