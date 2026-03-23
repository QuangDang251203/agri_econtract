package com.agribank.e_contract.controller;

import com.agribank.e_contract.entity.BankAccount;
import com.agribank.e_contract.response.ResponseList;
import com.agribank.e_contract.service.bankAccount.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank-accounts")
@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true" )
public class BankAccountController {
    private final BankAccountService bankAccountService;
    @PostMapping("/getBankAccountByBusinessCode/{businessCode}")
    public ResponseList<BankAccount> getBankAccountByBusinessCode(@PathVariable String businessCode) {
        return bankAccountService.getBankAccountByBusinessCode(businessCode);
    }
}
