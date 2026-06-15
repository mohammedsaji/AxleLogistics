package com.app.projectlogistics.UserDetailServices;

import com.app.projectlogistics.ValueObject.AccountVO;
import com.app.projectlogistics.ValueObject.EmployeeVO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class CustomizedUserDetails implements UserDetails {

    private AccountVO accountVO;

    public CustomizedUserDetails(AccountVO accountVO){
        this.accountVO = accountVO;
    }

    public String getUsername(){
        return accountVO.getAccountUsername();
    }

    public String getPassword(){
        return accountVO.getAccountPassword();
    }

    public List<SimpleGrantedAuthority> getAuthorities(){

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(accountVO.getAccountRole());
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
        simpleGrantedAuthorityList.add(simpleGrantedAuthority);

        return simpleGrantedAuthorityList;
    }

    public EmployeeVO getEmployeeInfo(){
        return accountVO.getEmployeeInfo();
    }

    public Integer getEmployeeId(){
        return accountVO.getEmployeeInfo().getEmployeeId();
    }

    public AccountVO getAccountVO(){
        return accountVO;
    }
}
