package com.agribank.e_contract.repository;

import com.agribank.e_contract.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findById(int id);

    @Query(value = "SELECT * FROM bank_account WHERE business_code = :businessCode",
            nativeQuery = true)
    List<BankAccount> findByBusinessCode(@Param("businessCode") String businessCode);
}
