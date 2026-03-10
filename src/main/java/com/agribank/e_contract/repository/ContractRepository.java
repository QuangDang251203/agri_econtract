package com.agribank.e_contract.repository;

import com.agribank.e_contract.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
        Contract findByContractCode(String contractCode);
}
