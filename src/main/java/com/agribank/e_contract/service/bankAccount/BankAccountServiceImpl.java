package com.agribank.e_contract.service.bankAccount;

import com.agribank.e_contract.entity.BankAccount;
import com.agribank.e_contract.mapper.BankAccountMapper;
import com.agribank.e_contract.repository.BankAccountRepository;
import com.agribank.e_contract.response.ResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepo;
    private final BankAccountMapper mapper;

    public ResponseList<BankAccount> getBankAccountByBusinessCode(String businessCode) {
        List<BankAccount> bankAccounts = bankAccountRepo.findByBusinessCode(businessCode);
        return ResponseList.success(bankAccounts);
    }
}
