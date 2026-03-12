package com.agribank.e_contract.controller;

import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.UpdateContractDTO;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.service.contract.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
public class ContractController {
    public final ContractService contractService;
    @PostMapping("/createContract")
    public CommonResponse createContract(@Valid @RequestBody ContractDTO dto) {
        return contractService.createContract(dto);
    }
    @PostMapping("/updateStatusFromManager")
    public CommonResponse updateStatus( @RequestBody UpdateContractDTO dto) {
        System.out.println("1111");
        return contractService.updateStatusFromManager(dto);
    }

    @PostMapping("/signContract")
    public CommonResponse signContract(@RequestParam String OtpCode,
                                       @RequestParam String contract) {
        return contractService.signContract(OtpCode, contract);
    }
}
