package com.app.logistics.employee.dto;

import com.app.logistics.auth.entity.Auth;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class EmployeeResponse {

    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private Integer employeeId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 250)
    private String employeeName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 25)
    private String employeePhoneNo;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 30)
    private String employeeDepartment;

    @NotNull(groups = OnCreate.class)
    private LocalDateTime employeeJoiningDate;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "ACTIVE|IN-ACTIVE")
    private String employeeStatus;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Positive
    @Max(value = Integer.MAX_VALUE)
    private Integer reportingManagerId;

    @NotNull(groups = OnCreate.class)
    @Positive
    @Max(value = Integer.MAX_VALUE)
    private Integer accountId;

    private Auth auth;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Max(value = Integer.MAX_VALUE)
    private Integer updatedBy;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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

    public Auth getAccountVO() {
        return auth;
    }

    public void setAccountVO(Auth auth) {
        this.auth = auth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
