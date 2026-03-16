package com.agribank.e_contract.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contract")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
    @Id
    private String contractCode;

    @ManyToOne
    @JoinColumn(name = "business_code")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "saving_book_id")
    private SavingBook savingBook;
    private int status;
    private BigDecimal loanAmount;
    private int loanTerm;
    private String paymentMethod;
    private float interestRate;

    @OneToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }
}
