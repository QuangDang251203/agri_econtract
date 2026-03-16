package com.agribank.e_contract.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "client")
@Data
public class Client {
    @Id
    @Column(name = "business_code")
    private String businessCode;

    private String businessName;
    private String email;
    private String phone;
    private String address;

    @OneToOne
    @JoinColumn(name = "cccd_id")
    @JsonIgnore
    private CCCD cccd;
}
