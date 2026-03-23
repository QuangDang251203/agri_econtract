package com.agribank.e_contract.repository;

import com.agribank.e_contract.entity.FileContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileContractRepository extends JpaRepository<FileContract, Integer> {
     FileContract findByContract_ContractCode(String contractCode);
     Optional<FileContract> findTopByContract_ContractCodeAndFileTypeOrderByIdDesc(
             String contractCode, String fileType
     );
   @Query(value = """
           SELECT fc.*
           FROM file_contract fc
           WHERE fc.contract_code = :contractCode
              AND fc.file_type IN ('stamped_pdf', 'signed_pdf', 'pdf')
           ORDER BY CASE
                      WHEN fc.file_type = 'stamped_pdf' THEN 1
                      WHEN fc.file_type = 'signed_pdf' THEN 2
                      WHEN fc.file_type = 'pdf' THEN 3
                      ELSE 4
                    END,
                    fc.id DESC
           LIMIT 1
           """, nativeQuery = true)
   Optional<FileContract> findFileByPriority(@Param("contractCode") String contractCode);
}
