package com.agribank.e_contract.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_account")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "business_code")
    private Client client;

    private String bankAccountNumber;
    private String branchName;
}
