package com.agribank.e_contract.controller;

import com.agribank.e_contract.dto.AccountDTO;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true" )
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public CommonResponse register(@RequestBody AccountDTO dto){
        return accountService.createAccount(dto);
    }

    @PostMapping("/login")
    public CommonResponse login(@RequestBody AccountDTO dto){
        return accountService.login(dto);
    }
}
