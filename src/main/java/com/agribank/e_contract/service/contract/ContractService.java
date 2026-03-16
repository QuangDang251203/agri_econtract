package com.agribank.e_contract.service.contract;

import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.ContractRequest;
import com.agribank.e_contract.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ContractService {
    String createContract(ContractDTO dto);
    ResponseEntity<byte[]> generateAndDownloadContract(String contractCode) throws IOException;
    CommonResponse signContract(String OtpCode, String contractCode);
    CommonResponse deleteContract(String contractCode);
    String moneyToWords(long amount);
    String SendOTP(String contractCode);
}
