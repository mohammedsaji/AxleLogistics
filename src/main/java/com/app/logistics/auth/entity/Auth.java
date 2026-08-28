package com.app.logistics.auth.entity;

import com.app.logistics.common.entity.BaseEntity;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.employee.entity.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "ACCNT_MST")
public class Auth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee-account-seq-gen")
    @SequenceGenerator(name = "employee-account-seq-gen",
            sequenceName = "ACCNT_MST_ACC_ID_SEQ",
            initialValue = 1,
            allocationSize = 50)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name = "ACC_ID")
    private Integer accountId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 63)
    @Column(name = "ACC_USERNAME")
    private String accountUsername;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Column(name = "ACC_PASSWORD")
    private String accountPassword;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "ADMIN|FEDERATE-DRIVER|FEDERATE-MANAGER|DEVELOPER|BUSINESS-ANALYST|DATA-ENGINEER|SOFTWARE ENGINEER")
    @Column(name = "ACC_ROLE")
    private String accountRole;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "ACTIVE|IN-ACTIVE")
    @Column(name = "ACC_STATUS")
    private String accountStatus;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Email(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 280)
    @Column(name = "ACCNT_EMAIL")
    private String accountEmail;

    @Max(value = Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @OneToOne(mappedBy = "auth", fetch = FetchType.EAGER)
    private Employee employee;

    public void setEmployeeVO(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployeeVO() {
        return employee;
    }

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

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
}