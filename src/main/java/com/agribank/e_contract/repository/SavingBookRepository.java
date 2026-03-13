package com.agribank.e_contract.repository;

import com.agribank.e_contract.entity.SavingBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SavingBookRepository extends JpaRepository<SavingBook, Long> {
    SavingBook findById(int id);
    @Query(value = "SELECT * FROM saving_book WHERE business_code = :businessCode AND status = 0",
            nativeQuery = true)
    List<SavingBook> findByBusinessCode(@Param("businessCode") String businessCode);

}
