package com.agribank.e_contract.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String representative;
    @OneToOne(mappedBy = "cccd")
    @JsonIgnore
    private Client client;
}
