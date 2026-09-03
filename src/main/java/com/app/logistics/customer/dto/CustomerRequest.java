package com.app.logistics.customer.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public class CustomerRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Integer customerId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 250, groups = {OnCreate.class, OnUpdate.class})
    private String customerName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Email(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 280, groups = {OnCreate.class, OnUpdate.class})
    private String customerEmail;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 25, groups = {OnCreate.class, OnUpdate.class})
    private String customerPhoneno;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhoneno() {
        return customerPhoneno;
    }

    public void setCustomerPhoneno(String customerPhoneno) {
        this.customerPhoneno = customerPhoneno;
    }
}