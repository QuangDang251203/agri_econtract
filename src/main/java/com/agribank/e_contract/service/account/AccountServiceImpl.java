package com.agribank.e_contract.service.account;

import com.agribank.e_contract.dto.AccountDTO;
import com.agribank.e_contract.entity.Account;
import com.agribank.e_contract.repository.AccountRepository;
import com.agribank.e_contract.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    public final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepo;

    public CommonResponse createAccount(AccountDTO dto) {
        log.info("[Begin]Create account with data request: {}", dto);
        Account account = new Account();
        account.setBusinessCode(dto.getBusinessCode());
        account.setUsername(dto.getUsername());
        account.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );
        account.setRole(1);
        accountRepo.save(account);
        return CommonResponse.success();
    }

    public CommonResponse login(AccountDTO dto) {
        Account account = accountRepo.findByBusinessCode(dto.getBusinessCode());
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        if (!passwordEncoder.matches(dto.getPassword(), account.getPassword())
                || !account.getUsername().equals(dto.getUsername())) {
            throw new RuntimeException("Password or username incorrect");
        }
        return CommonResponse.success();
    }
}
