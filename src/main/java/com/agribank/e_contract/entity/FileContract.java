package com.agribank.e_contract.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "file_contract")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "contract_code")
    private Contract contract;

    private String filePath;
    private String fileType;
    private LocalDate createdAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
