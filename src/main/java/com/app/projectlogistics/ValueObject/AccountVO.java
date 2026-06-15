package com.app.projectlogistics.ValueObject;

import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name="ACCNT_MST")
public class AccountVO {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="employee-account-seq-gen")
    @SequenceGenerator(name="employee-account-seq-gen",
            sequenceName = "ACCNT_MST_ACC_ID_SEQ",
            initialValue = 1,
            allocationSize = 50)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name="ACC_ID")
    private Integer accountId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 63)
    @Column(name = "ACC_USERNAME")
    private String accountUsername;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Column(name="ACC_PASSWORD")
    private String accountPassword;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "ADMIN|FEDERATE-DRIVER|FEDERATE-MANAGER|DEVELOPER|BUSINESS-ANALYST|DATA-ENGINEER|SOFTWARE ENGINEER")
    @Column(name="ACC_ROLE")
    private String accountRole;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp="ACTIVE|IN-ACTIVE")
    @Column(name="ACC_STATUS")
    private String accountStatus;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Email(groups = {OnCreate.class, OnUpdate.class})
    @Size(max=280)
    @Column(name="ACCNT_EMAIL")
    private String accountEmail;

    @Column(name="CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name="UPDATED_AT")
    private LocalDateTime updatedAt;

    @Max(value=Integer.MAX_VALUE)
    @Positive
    @Column(name="UPDATED_BY")
    private Integer updatedBy;

    @OneToOne(mappedBy = "accountVO", fetch = FetchType.EAGER)
    private EmployeeVO employeeVO;

    // --- Original Methods Preserved ---

    public void setEmployeeInfo(EmployeeVO employeeVO){
        this.employeeVO = employeeVO;
    }

    public EmployeeVO getEmployeeInfo(){
        return employeeVO;
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