package com.app.logistics.manager.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ManagerResponse {

    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private Integer managerId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 250)
    private String managerName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 20)
    private String managerContactNo;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private Integer operatorId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "ACTIVE|IN_ACTIVE")
    private String managerStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer updatedBy;


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getManagerStatus() {
        return managerStatus;
    }

    public void setManagerStatus(String managerStatus) {
        this.managerStatus = managerStatus;
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
