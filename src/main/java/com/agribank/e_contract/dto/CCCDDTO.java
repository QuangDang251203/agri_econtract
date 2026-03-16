package com.agribank.e_contract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CCCDDTO {

    @NotBlank(message = "CCCD Number is required")
    @Size(max = 12, min = 12, message = "CCCD Number must be 12 digits")
    private String CCCDNumber;

    @NotBlank(message = "Representative is required")
    private String representative;

    @NotBlank(message = "issuingLocation is required")
    private String issuingLocation;

    @NotBlank(message = "dateIssued is required")
    private String dateIssued;
}
