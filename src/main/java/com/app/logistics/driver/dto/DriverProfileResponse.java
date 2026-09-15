package com.app.logistics.driver.dto;

import com.app.logistics.account.dto.AccountResponse;
import com.app.logistics.manager.dto.ManagerResponse;
import com.app.logistics.operator.dto.OperatorResponse;

public class DriverProfileResponse {

    private AccountResponse accountResponse;

    private DriverResponse driverResponse;

    public ManagerResponse getManagerResponse() {
        return ManagerResponse;
    }

    public void setManagerResponse(ManagerResponse managerResponse) {
        ManagerResponse = managerResponse;
    }

    private ManagerResponse ManagerResponse;

    private OperatorResponse operatorResponse;

    public AccountResponse getAccountResponse() {
        return accountResponse;
    }

    public void setAccountResponse(AccountResponse accountResponse) {
        this.accountResponse = accountResponse;
    }

    public DriverResponse getDriverResponse() {
        return driverResponse;
    }

    public void setDriverResponse(DriverResponse driverResponse) {
        this.driverResponse = driverResponse;
    }

    public OperatorResponse getOperatorResponse() {
        return operatorResponse;
    }

    public void setOperatorResponse(OperatorResponse operatorResponse) {
        this.operatorResponse = operatorResponse;
    }
}
