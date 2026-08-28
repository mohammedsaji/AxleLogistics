package com.app.logistics.auth.authUtils;

import com.app.logistics.auth.repo.AuthRepo;
import com.app.logistics.auth.entity.Auth;
import com.app.logistics.utils.AuthDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final AuthRepo authRepo;

    public AuthService(AuthRepo authRepo){
        this.authRepo = authRepo;
    }

    @Override
    public AuthDetails loadUserByUsername(String username)throws UsernameNotFoundException {

        Auth auth = authRepo.findByAccountUsername(username);

        return new AuthDetails(auth);
    }
}
