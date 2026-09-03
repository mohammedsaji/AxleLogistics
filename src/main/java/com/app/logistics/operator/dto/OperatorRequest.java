package com.app.logistics.operator.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class OperatorRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer operatorId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 250, groups = {OnCreate.class, OnUpdate.class})
    private String operatorName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 20, groups = {OnCreate.class, OnUpdate.class})
    private String operatorTransportType;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    private Integer managerId;

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
}