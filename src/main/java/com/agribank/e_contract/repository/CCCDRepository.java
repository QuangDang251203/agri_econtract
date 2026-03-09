package com.agribank.e_contract.repository;

import com.agribank.e_contract.entity.CCCD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CCCDRepository extends JpaRepository<CCCD, Integer> {
}
