package com.app.logistics.driver.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class DriverRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Positive(groups = OnUpdate.class)
    private Integer driverId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 250, groups = {OnCreate.class, OnUpdate.class})
    private String driverName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 25, groups = {OnCreate.class, OnUpdate.class})
    @Pattern(
            regexp = "^[0-9+()\\-\\s]+$",
            message = "Invalid phone number",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String driverPhoneNo;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 20, groups = {OnCreate.class, OnUpdate.class})
    private String driverLicenseNo;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    private Integer operatorId;

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
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

    public String getDriverLicenseNo() {
        return driverLicenseNo;
    }

    public void setDriverLicenseNo(String driverLicenseNo) {
        this.driverLicenseNo = driverLicenseNo;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
}