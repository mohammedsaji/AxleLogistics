package com.app.logistics.employee.dto;

import com.app.logistics.account.dto.AccountResponse;

public class EmployeeProfileResponse {

    private AccountResponse accountResponse;

    private EmployeeResponse employeeResponse;

    public AccountResponse getAccountResponse() {
        return accountResponse;
    }

    public void setAccountResponse(AccountResponse accountResponse) {
        this.accountResponse = accountResponse;
    }

    public EmployeeResponse getEmployeeResponse() {
        return employeeResponse;
    }

    public void setEmployeeResponse(EmployeeResponse employeeResponse) {
        this.employeeResponse = employeeResponse;
    }
}
