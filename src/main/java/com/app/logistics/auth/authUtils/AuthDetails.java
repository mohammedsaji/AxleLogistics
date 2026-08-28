package com.app.logistics.auth.authUtils;

import com.app.logistics.auth.entity.Auth;
import com.app.logistics.employee.entity.Employee;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class AuthDetails implements UserDetails {

    private final Auth auth;

    public AuthDetails(Auth auth){
        this.auth = auth;
    }

    public String getUsername(){
        return auth.getAccountUsername();
    }

    public String getPassword(){
        return auth.getAccountPassword();
    }

    public List<SimpleGrantedAuthority> getAuthorities(){

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(auth.getAccountRole());
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
        simpleGrantedAuthorityList.add(simpleGrantedAuthority);

        return simpleGrantedAuthorityList;
    }

    public Employee getEmployeeInfo(){
        return auth.getEmployeeVO();
    }

    public Integer getEmployeeId(){
        return auth.getEmployeeVO().getEmployeeId();
    }

    public Auth getAccountVO(){
        return auth;
    }
}
