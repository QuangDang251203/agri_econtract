package com.agribank.e_contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractCodeDTO {
    private int clientId;
    private int branchId;
    private int savingBookId;
    private LocalDate createdAt;
}
