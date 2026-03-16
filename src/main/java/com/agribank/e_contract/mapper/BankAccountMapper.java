package com.agribank.e_contract.mapper;

import com.agribank.e_contract.dto.BankAccountDTO;
import com.agribank.e_contract.entity.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {
    public BankAccountDTO toDTO(BankAccount bankAccount) {
        if (bankAccount == null) {
            return null;
        }
        BankAccountDTO dto = new BankAccountDTO();
        dto.setBankAccountNumber(bankAccount.getBankAccountNumber());
        dto.setBranchName(bankAccount.getBranchName());
        dto.setBusinessCode(bankAccount.getClient().getBusinessCode());
        return dto;
    }
}
