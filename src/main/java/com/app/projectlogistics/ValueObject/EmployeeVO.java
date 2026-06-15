package com.app.projectlogistics.ValueObject;

import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="EMP_MST", indexes={
        @Index(name="IDX_EMP_MST_ACC_ID", columnList ="ACC_ID")})
public class EmployeeVO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipping-employee-master-seq-gen")
    @SequenceGenerator(name = "shipping-employee-master-seq-gen",
            sequenceName = "EMP_MST_EMP_ID_SEQ",
            initialValue = 1,
            allocationSize = 50)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name = "EMP_ID")
    private Integer employeeId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=250)
    @Column(name = "EMP_NAME")
    private String employeeName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=25)
    @Column(name = "EMP_PHNE_NO")
    private String employeePhoneNo;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=30)
    @Column(name = "EMP_DEPARTMENT")
    private String employeeDepartment;

    @Column(name = "EMP_JOINING_DATE")
    private LocalDateTime employeeJoiningDate;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp="ACTIVE|IN-ACTIVE", groups = {OnCreate.class, OnUpdate.class})
    @Column(name = "EMP_STATUS")
    private String employeeStatus;

    @Positive(groups = {OnCreate.class, OnUpdate.class})
    @Max(value = Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    @Column(name = "REPORTING_MANAGER_ID")
    private Integer reportingManagerId;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JoinColumn(name="ACC_ID")
    @OneToOne(fetch = FetchType.EAGER)
    private AccountVO accountVO;

    public void setAccountVO(AccountVO accountVO){
        this.accountVO = accountVO;
    }

    public AccountVO getAccountVO(){
        return accountVO;
    }

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Max(value=Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getEmployeeJoiningDate() {
        return employeeJoiningDate;
    }

    public void setEmployeeJoiningDate(LocalDateTime employeeJoiningDate) {
        this.employeeJoiningDate = employeeJoiningDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeePhoneNo() {
        return employeePhoneNo;
    }

    public void setEmployeePhoneNo(String employeePhoneNo) {
        this.employeePhoneNo = employeePhoneNo;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Integer getReportingManagerId() {
        return reportingManagerId;
    }

    public void setReportingManagerId(Integer reportingManagerId) {
        this.reportingManagerId = reportingManagerId;
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