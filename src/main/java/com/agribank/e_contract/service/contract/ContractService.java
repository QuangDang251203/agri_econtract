package com.agribank.e_contract.service.contract;

import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.ContractDetailDTO;
import com.agribank.e_contract.dto.AllContractDTO;
import com.agribank.e_contract.entity.Contract;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.response.ResponseList;
import com.agribank.e_contract.response.ResponseObject;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@Service
public interface ContractService {
    String createContract(ContractDTO dto);
    ResponseEntity<byte[]> generateAndDownloadContract(String contractCode) throws IOException;
    CommonResponse signContract(String OtpCode, String contractCode);
    CommonResponse deleteContract(String contractCode);
    String moneyToWords(long amount);
    String SendOTP(String contractCode);
    ResponseList<Contract> getContractByBusinessCode(String businessCode);
    ResponseList<AllContractDTO> getAllContracts();
    ResponseObject<ContractDetailDTO> getContractDetailByContractCode(String contractCode) throws IOException;
    CommonResponse signContractWithSignature(String contractCode,
                                             String otpCode,
                                             MultipartFile signatureFile) throws IOException;
    CommonResponse approveContract(String contractCode) throws IOException;
    CommonResponse rejectContract(String contractCode);
    Resource getContractFileResource(String contractCode) throws MalformedURLException;
}
