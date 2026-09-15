package com.app.logistics.auth.authUtils;

import com.app.logistics.account.entity.Account;
import com.app.logistics.account.repo.AccountRepo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final AccountRepo accountRepo;

    public AuthService(AccountRepo accountRepo){
        this.accountRepo = accountRepo;
    }

    @Override
    public AuthDetails loadUserByUsername(String username)throws UsernameNotFoundException {

        Account account = accountRepo.findByAccountUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new AuthDetails(account);
    }
}
