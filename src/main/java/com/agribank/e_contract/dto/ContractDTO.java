package com.agribank.e_contract.dto;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {

    private String contractCode;

    @NotNull(message = "Client id is required")
    private int clientId;

    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Branch id is required")
    private int branchId;

    @NotNull(message = "Loan Amount is required")
    private BigDecimal loanAmount;

    @NotNull(message = "Loan Term is required")
    private int loanTerm;

    @NotNull(message = "Interest Rate is required")
    @DecimalMax(value = "0.06", message = "Interest rate must not exceed 6%")
    @DecimalMin(value = "0.05", message = "Interest rate must be at least 5%")
    private float interestRate;

    @NotNull(message = "Saving Book id is required")
    private int savingBookId;

    private Integer status;
    private LocalDate createdAt;
}
