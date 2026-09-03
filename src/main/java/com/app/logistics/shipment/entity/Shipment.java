package com.app.logistics.shipment.entity;

import com.app.logistics.cargo.entity.Cargo;
import com.app.logistics.common.entity.BaseEntity;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.customer.entity.Customer;
import com.app.logistics.shipmentStatus.entity.ShipmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SHIPPNG_INFO", indexes = {
        @Index( name="IDX_SHIPPNG_INFO_SHIPPNG_CRGO_ID", columnList="SHIPPNG_CRGO_ID"),
        @Index( name="IDX_SHIPPNG_INFO_CUSTMER_USR_ID" , columnList="CUSTMER_USR_ID")
})
public class Shipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipping-info-seq_gen")
    @SequenceGenerator(name = "shipping-info-seq_gen",
            sequenceName = "SHIP_INFO_SHIPPNG_ID_SEQ",
            initialValue = 1,
            allocationSize = 50)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name="SHIPPNG_ID")
    private Integer shippingId;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JoinColumn( name = "SHIPPNG_CRGO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cargo Cargo;

    public Cargo getShippingCargoInfoVO() {
        return Cargo;
    }

    public void setShippingCargoInfoVO(Cargo Cargo) {
        this.Cargo = Cargo;
    }

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=50)
    @Column( name = "SHIPPNG_FROM")
    private String shippingFrom;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=50)
    @Column(name = "SHIPPNG_TO")
    private String shippingTo;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JoinColumn(name = "SHIPPNG_STATS_ID")
    @OneToOne( fetch = FetchType.LAZY)
    private ShipmentStatus shipmentStatus;

    public ShipmentStatus getShippingStatusInfoVO() {
        return shipmentStatus;
    }

    public void setShippingStatusInfoVO(ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JoinColumn(name="CUSTMER_USR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public Customer getShippingForUserVO() {
        return customer;
    }

    public void setShippingForUserVO(Customer customer) {
        this.customer = customer;
    }

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Column(name="EXPECTED_DELIVERY_DATE")
    private LocalDateTime deliveryDate;

    @Column(name = "UPDATED_BY")
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    @Max(value = Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    private Integer updatedBy;

    public String getShippingFrom() {
        return shippingFrom;
    }

    public void setShippingFrom(String shippingFrom) {
        this.shippingFrom = shippingFrom;
    }

    public Integer getShippingId() {
        return shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
    }

    public String getShippingTo() {
        return shippingTo;
    }

    public void setShippingTo(String shippingTo) {
        this.shippingTo = shippingTo;
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
}