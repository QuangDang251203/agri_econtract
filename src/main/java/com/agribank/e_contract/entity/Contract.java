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
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "saving_book_id")
    private SavingBook savingBook;

    private String email;
    private int status;
    private BigDecimal loanAmount;
    private int loanTerm; // in months
    private float interestRate;
    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }
}
