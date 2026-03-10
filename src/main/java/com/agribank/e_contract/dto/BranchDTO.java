package com.agribank.e_contract.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {

    private int id;
    @NotBlank(message = "Branch Name is required")
    private String branchName;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.05", message = "Interest rate must be at least 5%")
    @DecimalMax(value = "0.06", message = "Interest rate must not exceed 6%")
    private float interestRate;
}
