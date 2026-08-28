package com.app.logistics.cargo.repo;

import com.app.logistics.cargo.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepo extends JpaRepository<Cargo,Integer> {
}
