package com.agribank.e_contract.service.bankAccount;

import com.agribank.e_contract.entity.BankAccount;
import com.agribank.e_contract.response.ResponseList;
import org.springframework.stereotype.Service;

@Service
public interface BankAccountService {
    ResponseList<BankAccount> getBankAccountByBusinessCode(String businessCode);
}
