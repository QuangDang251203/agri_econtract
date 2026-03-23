package com.agribank.e_contract.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateContractDTO {
    private String contractCode;

    @NotNull(message = "Status is required")
    private Integer status;
}
