package com.agribank.e_contract.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
