package com.agribank.e_contract.service.contract;

import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.UpdateContractDTO;
import com.agribank.e_contract.response.CommonResponse;
import org.springframework.stereotype.Service;

@Service
public interface ContractService {
    CommonResponse createContract(ContractDTO dto);

    CommonResponse signContract(String OtpCode, String contractCode);
    CommonResponse deleteContract(String contractCode);
}
