package com.agribank.e_contract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingBookDTO {
    private int id;
    private String balance;

    @NotNull(message = "Status is required")
    private int status; // 0: inactive, 1: active, 2: closed

    @NotNull(message = "Client id is required")
    private int clientId;
}
