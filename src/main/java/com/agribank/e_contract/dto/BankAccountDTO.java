package com.agribank.e_contract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {

    @NotBlank(message = "Business code is required")
    private String businessCode;

    @NotBlank(message = "Bank account number is required")
    @Size(min = 13, max = 13, message = "Bank account number must be exactly 13 characters")
    private String bankAccountNumber;

    @NotBlank(message = "Branch name is required")
    private String branchName;
}
