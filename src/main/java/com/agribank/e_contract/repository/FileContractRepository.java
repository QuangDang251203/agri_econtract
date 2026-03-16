package com.agribank.e_contract.repository;

import com.agribank.e_contract.entity.FileContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileContractRepository extends JpaRepository<FileContract, Integer> {
     FileContract findByContract_ContractCode(String contractCode);
}
