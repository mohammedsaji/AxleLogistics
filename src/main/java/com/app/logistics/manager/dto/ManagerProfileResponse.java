package com.app.logistics.manager.dto;

import com.app.logistics.account.dto.AccountResponse;
import com.app.logistics.operator.dto.OperatorResponse;

public class ManagerProfileResponse {

    private AccountResponse accountResponse;

    private ManagerResponse managerResponse;

    private OperatorResponse operatorResponse;

    public AccountResponse getAccountResponse() {
        return accountResponse;
    }

    public void setAccountResponse(AccountResponse accountResponse) {
        this.accountResponse = accountResponse;
    }

    public ManagerResponse getManagerResponse() {
        return managerResponse;
    }

    public void setManagerResponse(ManagerResponse managerResponse) {
        this.managerResponse = managerResponse;
    }

    public OperatorResponse getOperatorResponse() {
        return operatorResponse;
    }

    public void setOperatorResponse(OperatorResponse operatorResponse) {
        this.operatorResponse = operatorResponse;
    }
}
