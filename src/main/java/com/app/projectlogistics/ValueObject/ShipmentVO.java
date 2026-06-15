package com.app.projectlogistics.ValueObject;

import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SHIPPNG_INFO", indexes = {
        @Index( name="IDX_SHIPPNG_INFO_SHIPPNG_CRGO_ID", columnList="SHIPPNG_CRGO_ID"),
        @Index( name="IDX_SHIPPNG_INFO_CUSTMER_USR_ID" , columnList="CUSTMER_USR_ID")
})
public class ShipmentVO {

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
    private CargoVO CargoVO;

    public CargoVO getShippingCargoInfoVO() {
        return CargoVO;
    }

    public void setShippingCargoInfoVO(CargoVO CargoVO) {
        this.CargoVO = CargoVO;
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
    private ShipmentStatusVO shipmentStatusVO;

    public ShipmentStatusVO getShippingStatusInfoVO() {
        return shipmentStatusVO;
    }

    public void setShippingStatusInfoVO(ShipmentStatusVO shipmentStatusVO) {
        this.shipmentStatusVO = shipmentStatusVO;
    }

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JoinColumn(name="CUSTMER_USR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerVO customerVO;

    public CustomerVO getShippingForUserVO() {
        return customerVO;
    }

    public void setShippingForUserVO(CustomerVO customerVO) {
        this.customerVO = customerVO;
    }

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Column(name="EXPECTED_DELIVERY_DATE")
    private LocalDateTime deliveryDate;

    @Column(name="CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name="UPDATED_AT")
    private LocalDateTime updatedAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}