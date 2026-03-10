package com.agribank.e_contract.mapper;

import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.OTPDTO;
import com.agribank.e_contract.entity.OTP;
import com.agribank.e_contract.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OTPMapper {
    public final ContractRepository contractRepo;
    public OTPDTO fromContractDTOToOTPDTO(ContractDTO contractDTO) {
        OTPDTO otpDTO = new OTPDTO();
        otpDTO.setEmail(contractDTO.getEmail());
        otpDTO.setContractCode(contractDTO.getContractCode());
        return otpDTO;
    }
    public OTP toEntity(OTPDTO otpDTO) {
        if (otpDTO == null) {
            return null;
        }
        OTP otp = new OTP();
        otp.setOtpCode(otpDTO.getOtpCode());
        otp.setEmail(otpDTO.getEmail());
        if(otpDTO.getContractCode() != null) {
            otp.setContract(contractRepo.findByContractCode(otpDTO.getContractCode()));
        }
        otp.setUsed(otpDTO.isUsed());
        otp.setCreatedAt(otpDTO.getCreatedAt());
        otp.setExpiredAt(otpDTO.getExpiredAt());
        return otp;
    }

}
