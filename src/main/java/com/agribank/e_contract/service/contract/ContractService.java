package com.agribank.e_contract.service.contract;

import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.ContractRequest;
import com.agribank.e_contract.entity.Contract;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.response.ResponseList;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ContractService {
    String createContract(ContractDTO dto);
    ResponseEntity<byte[]> generateAndDownloadContract(String contractCode) throws IOException;
    CommonResponse signContract(String OtpCode, String contractCode);
    CommonResponse deleteContract(String contractCode);
    String moneyToWords(long amount);
    String SendOTP(String contractCode);
    ResponseList<Contract> getContractByBusinessCode(String businessCode);
    ResponseList<Contract> getAllContracts();
}
