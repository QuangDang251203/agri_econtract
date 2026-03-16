package com.agribank.e_contract.controller;

import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.service.contract.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true")
public class ContractController {
    public final ContractService contractService;

    @PostMapping("/createContract")
    public CommonResponse createContract(@Valid @RequestBody ContractDTO dto) {
        return contractService.createContract(dto);
    }

    @PostMapping("/signContract")
    public CommonResponse signContract(@RequestParam String OtpCode,
                                       @RequestParam String contract) {
        return contractService.signContract(OtpCode, contract);
    }

    @PutMapping("/deleteContract/{contractCode}")
    public CommonResponse deleteContract(@PathVariable String contractCode) {
        return contractService.deleteContract(contractCode);
    }

    @PostMapping("/generate-and-download/{contractCode}")
    public ResponseEntity<byte[]> generateAndDownloadContract(@PathVariable String contractCode ) throws IOException {
        return contractService.generateAndDownloadContract(contractCode);
    }
}


