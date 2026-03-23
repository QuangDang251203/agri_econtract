package com.agribank.e_contract.repository;

import com.agribank.e_contract.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {
        Contract findByContractCode(String contractCode);
        List<Contract> findByClientBusinessCode(String businessCode);
}
