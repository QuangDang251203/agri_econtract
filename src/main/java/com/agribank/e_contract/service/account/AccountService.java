package com.agribank.e_contract.service.account;

import com.agribank.e_contract.dto.AccountDTO;
import com.agribank.e_contract.response.CommonResponse;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    CommonResponse createAccount(AccountDTO dto);
    CommonResponse login(AccountDTO dto);
}
