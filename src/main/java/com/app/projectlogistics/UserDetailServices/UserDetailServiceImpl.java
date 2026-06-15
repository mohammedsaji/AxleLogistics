package com.app.projectlogistics.UserDetailServices;

import com.app.projectlogistics.Repository.UserDetailServicesRepo.UserDetailServiceImplRepo;
import com.app.projectlogistics.ValueObject.AccountVO;
import com.app.projectlogistics.ValueObject.EmployeeVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private UserDetailServiceImplRepo userDetailServiceImplRepo;

    public UserDetailServiceImpl(UserDetailServiceImplRepo userDetailServiceImplRepo){
        this.userDetailServiceImplRepo = userDetailServiceImplRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException {

        AccountVO accountVO = userDetailServiceImplRepo.findByAccountUsername(username);

        return new CustomizedUserDetails(accountVO);
    }
}
