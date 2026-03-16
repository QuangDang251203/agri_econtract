package com.agribank.e_contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileContractDTO {
    private int id;
    private String filePath;
    private String fileType;
    private String contractCode;
    private LocalDate createdAt;
}
