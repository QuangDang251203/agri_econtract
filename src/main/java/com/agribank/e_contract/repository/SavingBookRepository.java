package com.agribank.e_contract.repository;

import com.agribank.e_contract.entity.SavingBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingBookRepository extends JpaRepository<SavingBook, Long> {
    SavingBook findById(int id);
    List<SavingBook> findByClientId(int clientId);
}
