package com.app.logistics.account.repo;

import com.app.logistics.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account,Integer> {

    public Account findByAccountUsername(String username);

    public Optional<Account> findByAccountId(Integer accountId);

    public Optional<Account> findByAccountEmail(String signedUpUserGmailId);
}
