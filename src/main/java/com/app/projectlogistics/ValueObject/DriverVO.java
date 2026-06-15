package com.app.projectlogistics.ValueObject;

import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="DRVR_MST", indexes={
        @Index(name="IDX_DRVR_MST_OPRTR_ID", columnList="OPRTR_ID")
})
public class DriverVO {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="shipping-driver-master-seq-gen")
    @SequenceGenerator(name="shipping-driver-master-seq-gen",
            sequenceName = "DRVR_MST_DRVR_ID_SEQ",
            initialValue= 1,
            allocationSize=50
    )
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    @Column(name="DRVR_ID")
    private Integer driverId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=250)
    @Column(name="DRVR_NAME")
    private String driverName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=250)
    @Column(name="DRVR_PHONE")
    private String driverPhoneNo;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=20)
    @Column(name="DRVR_LICNSE_NO")
    private String driverLicenseNo;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JoinColumn(name="OPRTR_ID")
    @ManyToOne( fetch = FetchType.LAZY)
    private OperatorVO operatorVO;

    public OperatorVO getOperatorVO() {
        return operatorVO;
    }

    public void setOperatorVO(OperatorVO operatorVO) {
        this.operatorVO = operatorVO;
    }

    @JoinColumn(name="ACC_ID")
    @OneToOne( fetch = FetchType.EAGER)
    private AccountVO accountVO;

    public AccountVO getAccountVO() {
        return accountVO;
    }

    public void setAccountVO(AccountVO accountVO) {
        this.accountVO = accountVO;
    }

    @Column(name="CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name="UPDATED_AT")
    private LocalDateTime updatedAt;

    @Max(value=Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    @Column(name="UPDATED_BY")
    private Integer updatedBy;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getDriverLicenseNo() {
        return driverLicenseNo;
    }

    public void setDriverLicenseNo(String driverLicenseNo) {
        this.driverLicenseNo = driverLicenseNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhoneNo() {
        return driverPhoneNo;
    }

    public void setDriverPhoneNo(String driverPhoneNo) {
        this.driverPhoneNo = driverPhoneNo;
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