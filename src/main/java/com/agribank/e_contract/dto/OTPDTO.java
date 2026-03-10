package com.agribank.e_contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPDTO {
    private String otpCode;
    private String email;
    private String contractCode;
    private boolean isUsed;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
}
