package com.app.logistics.operator.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class OperatorResponse {

    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private Integer operatorId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 250)
    private String operatorName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 20)
    private String operatorTransportType;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private Integer managerId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Max(value = Integer.MAX_VALUE)
    private Integer updatedBy;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorTransportType() {
        return operatorTransportType;
    }

    public void setOperatorTransportType(String operatorTransportType) {
        this.operatorTransportType = operatorTransportType;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
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
