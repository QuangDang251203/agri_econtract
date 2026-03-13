package com.agribank.e_contract.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "saving_book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private BigDecimal balance;
    private int status; // 0: inactive, 1: active, 2: closed
    private int duration; // in months
    private LocalDate createdAt;
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }

    @ManyToOne
    @JoinColumn(name = "business_code")
    private Client client;
}
