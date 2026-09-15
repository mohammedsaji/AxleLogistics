package com.app.logistics.auth.authUtils;

import com.app.logistics.account.entity.Account;
import com.app.logistics.employee.entity.Employee;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthDetails implements UserDetails {

    private final Account account;

    public AuthDetails(Account account){
        this.account = account;
    }

    @Override
    public String getUsername() {
        return account != null ? account.getAccountUsername() : null;
    }

    @Override
    public String getPassword() {
        return account != null ? account.getAccountPassword() : null;
    }

    public List<SimpleGrantedAuthority> getAuthorities(){
        if (account == null || account.getAccountRole() == null) {
            return Collections.emptyList();
        }
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(account.getAccountRole());
        return Collections.singletonList(new SimpleGrantedAuthority(account.getAccountRole()));
    }

    public Employee getEmployeeInfo() {
        return (account != null) ? account.getEmployee() : null;
    }

    public Integer getEmployeeId() {
        if (account != null && account.getEmployee() != null) {
            return account.getEmployee().getEmployeeId();
        }
        return null;
    }
    public Account getAccount(){
        return account;
    }
}
