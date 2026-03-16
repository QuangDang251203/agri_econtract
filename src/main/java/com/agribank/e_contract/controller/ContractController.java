package com.agribank.e_contract.controller;

import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.entity.Contract;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.response.ResponseList;
import com.agribank.e_contract.service.contract.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = {"contractCode", "Contract-Code", "X-Contract-Code"})

public class ContractController {
    public final ContractService contractService;

    @PostMapping("/signContract")
    public CommonResponse signContract(@RequestParam String OtpCode,
                                       @RequestParam String contract) {
        return contractService.signContract(OtpCode, contract);
    }

    @PutMapping("/deleteContract/{contractCode}")
    public CommonResponse deleteContract(@PathVariable String contractCode) {
        return contractService.deleteContract(contractCode);
    }

    @PostMapping("/createAndGenerateContract")
    public ResponseEntity<byte[]> createAndGenerateContract(@Valid @RequestBody ContractDTO dto) throws IOException {
        String contractCode = contractService.createContract(dto);
        return contractService.generateAndDownloadContract(contractCode);
    }

    @PostMapping("/money-to-words")
    public String moneyToWords(@RequestParam long amount) {
        return contractService.moneyToWords(amount);
    }

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam String contractCode) {
        return contractService.SendOTP(contractCode);
    }

    @PostMapping("/getContractByBusinessCode/{businessCode}")
    public ResponseList<Contract> getContractByBusinessCode(@PathVariable String businessCode) {
        return contractService.getContractByBusinessCode(businessCode);
    }

    @PostMapping("/getAllContracts")
    public ResponseList<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }
}


