package com.app.logistics.manager.entity;

import com.app.logistics.auth.entity.Auth;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.operator.entity.Operator;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name= "OPRTR_MNGR_MST")
public class Manager {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name="OPRTR_MNGR_ID")
    private Integer managerId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=250)
    @Column(name="OPRTR_MNGR_NAME")
    private String managerName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=20)
    @Column(name="OPRTR_MNGR_CONTACT_NUMBER")
    private String managerContactNo;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JoinColumn(name="OPRTR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator operator;

    public Operator getOperatorVO() {
        return operator;
    }

    public void setOperatorVO(Operator operator) {
        this.operator = operator;
    }

    @JoinColumn(name="ACC_ID")
    @OneToOne( fetch = FetchType.EAGER)
    private Auth auth;

    public Auth getAccountVO() {
        return auth;
    }

    public void setAccountVO(Auth auth) {
        this.auth = auth;
    }

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "ACTIVE|IN_ACTIVE", groups = {OnCreate.class, OnUpdate.class})
    private String managerStatus;

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

    public String getManagerStatus() {
        return managerStatus;
    }

    public void setManagerStatus(String managerStatus) {
        this.managerStatus = managerStatus;
    }

    public String getManagerContactNo() {
        return managerContactNo;
    }

    public void setManagerContactNo(String managerContactNo) {
        this.managerContactNo = managerContactNo;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
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