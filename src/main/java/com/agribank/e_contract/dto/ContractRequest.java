package com.agribank.e_contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {
    private String businessName;
    private String businessCode;
    private String address;
    private String phoneNumber;
    private String branchName;
    private String bankAccountNumber;
    private String representative;
    private String cccdNumber;
    private String issuingLocation;
    private LocalDate dateIssued;
    private BigDecimal loanAmount;
    private int loanTerm;
    private String loanAmountInWords;
    private float interestRate;
    private int savingBookId;
    private BigDecimal balance;
    private String balenceInWords;
    private LocalDate dateOfDeposit;
    private int duration;
    private LocalDate createdAt;

    public LocalDate calculateMaturityDate() {
        return dateOfDeposit.plusMonths(duration);
    }
}
