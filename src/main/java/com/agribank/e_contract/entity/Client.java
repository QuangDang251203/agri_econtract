package com.agribank.e_contract.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "client")
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fullName;
    private String email;
    private Date birthDate;
    private String phone;
    private String address;

    @OneToOne
    @JoinColumn(name = "cccd_id")
    private CCCD cccd;
}
