package com.app.projectlogistics.DataTransferObject.Operator;

import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class RSPOperatorDTO {

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
