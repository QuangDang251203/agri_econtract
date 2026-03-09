package com.agribank.e_contract.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class UpdateInterestRateDTO {
    @DecimalMin(value = "0.05")
    @DecimalMax(value = "0.06")
    private float interestRate;
}
