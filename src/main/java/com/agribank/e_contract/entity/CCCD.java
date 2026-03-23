package com.agribank.e_contract.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "cccd")
@Data
public class CCCD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String cccdNumber;
    private String issuingLocation;
    private String representative;
    private LocalDate dateIssued;
    @OneToOne(mappedBy = "cccd")
    @JsonIgnore
    private Client client;
}
