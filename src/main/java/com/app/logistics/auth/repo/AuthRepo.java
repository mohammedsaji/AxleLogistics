package com.app.logistics.auth.repo;

import com.app.logistics.auth.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepo extends JpaRepository<Auth,Integer> {

    public Auth findByAccountUsername(String username);
}
