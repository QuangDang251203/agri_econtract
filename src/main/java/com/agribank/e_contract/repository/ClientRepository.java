package com.agribank.e_contract.repository;

import com.agribank.e_contract.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
        Client findClientByBusinessCode(String businessCode);
}
