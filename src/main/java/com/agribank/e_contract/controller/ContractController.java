package com.agribank.e_contract.controller;

import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.ContractDetailDTO;
import com.agribank.e_contract.dto.AllContractDTO;
import com.agribank.e_contract.entity.Contract;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.response.ResponseList;
import com.agribank.e_contract.response.ResponseObject;
import com.agribank.e_contract.service.contract.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @PostMapping(value = "/sign-with-signature", consumes = "multipart/form-data")
    public CommonResponse signWithSignature(@RequestParam String contractCode,
                                            @RequestParam String otpCode,
                                            @RequestPart("signatureFile") org.springframework.web.multipart.MultipartFile signatureFile) throws IOException {
        return contractService.signContractWithSignature(contractCode, otpCode, signatureFile);
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
    public ResponseList<AllContractDTO> getAllContracts() {
        return contractService.getAllContracts();
    }

    @GetMapping("/getContractDetailByContractCode/{contractCode}")
    public ResponseObject<ContractDetailDTO> getContractDetailByContractCode(@PathVariable String contractCode) throws IOException {
        return contractService.getContractDetailByContractCode(contractCode);
    }

    @GetMapping("/getContractFileByContractCode/{contractCode}")
    public ResponseEntity<Resource> getContractFileByContractCode(@PathVariable String contractCode) throws Exception {
        Resource resource = contractService.getContractFileResource(contractCode);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"contract.pdf\"")
                .body(resource);
    }

}


