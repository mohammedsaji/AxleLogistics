package com.app.logistics.auth.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthRequest {

    @Null(groups = OnCreate.class)
    @NotBlank(groups = OnUpdate.class)
    private Integer accountId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 63)
    private String accountUsername;

    @NotBlank(groups = OnCreate.class)
    @Size(min = 8, max = 20)
    private String accountPassword;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(
            regexp = "ADMIN|FEDERATE-DRIVER|FEDERATE-MANAGER|DEVELOPER|BUSINESS-ANALYST|DATA-ENGINEER|SOFTWARE ENGINEER",
            message = "Invalid account role"
    )
    private String accountRole;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(
            regexp = "ACTIVE|IN-ACTIVE",
            message = "Account status must be ACTIVE or IN-ACTIVE"
    )
    private String accountStatus;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Email(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 280)
    private String accountEmail;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(String accountUsername) {
        this.accountUsername = accountUsername;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(String accountRole) {
        this.accountRole = accountRole;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }
}