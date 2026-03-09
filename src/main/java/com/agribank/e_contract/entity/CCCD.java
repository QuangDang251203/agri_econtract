package com.agribank.e_contract.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cccd")
@Data
public class CCCD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String cccdNumber;
    private String issuingLocation;

    @OneToOne(mappedBy = "cccd")
    private Client client;
}
