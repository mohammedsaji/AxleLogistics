package com.app.logistics.customer.dto;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnShipmentSave;
import com.app.logistics.common.validations.OnUpdate;
import jakarta.validation.constraints.*;

public class CustomerRequest{

    @Null(groups = OnShipmentSave.class)
    @NotNull(groups = OnUpdate.class)
    private Integer customerId;

    @NotBlank(groups = OnShipmentSave.class)
    @Size(max = 250, groups = OnShipmentSave.class)
    private String customerName;

    @NotBlank(groups = OnShipmentSave.class)
    @Email(groups = OnShipmentSave.class)
    @Size(max = 280, groups = OnShipmentSave.class)
    private String customerEmail;

    @NotBlank(groups = OnShipmentSave.class)
    @Size(max = 25, groups = OnShipmentSave.class)
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