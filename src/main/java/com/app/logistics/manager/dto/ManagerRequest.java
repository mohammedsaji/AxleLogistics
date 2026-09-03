package com.app.logistics.manager.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ManagerRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer managerId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 250, groups = {OnCreate.class, OnUpdate.class})
    private String managerName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 20, groups = {OnCreate.class, OnUpdate.class})
    private String managerContactNo;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    private Integer operatorId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(
            regexp = "ACTIVE|IN_ACTIVE",
            message = "Manager status must be ACTIVE or IN_ACTIVE",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String managerStatus;

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

    public String getManagerContactNo() {
        return managerContactNo;
    }

    public void setManagerContactNo(String managerContactNo) {
        this.managerContactNo = managerContactNo;
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
}