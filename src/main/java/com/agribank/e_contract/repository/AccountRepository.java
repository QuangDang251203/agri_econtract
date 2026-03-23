package com.agribank.e_contract.repository;

import com.agribank.e_contract.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByBusinessCode(String businessCode);
}
