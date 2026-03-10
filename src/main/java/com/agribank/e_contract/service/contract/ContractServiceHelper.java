package com.agribank.e_contract.service.contract;

import com.agribank.e_contract.constant.MailConstant;
import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.OTPDTO;
import com.agribank.e_contract.mapper.OTPMapper;
import com.agribank.e_contract.repository.OTPRepository;
import com.agribank.e_contract.service.mail.MailService;
import com.agribank.e_contract.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ContractServiceHelper {
    public final OTPMapper otpMapper;
    public final OTPRepository otpRepo;
    public final MailService mailService;

    public void sendAndSaveOTP(ContractDTO dto){
        OTPDTO otpdto = otpMapper.fromContractDTOToOTPDTO(dto);
        String otpCode = CommonUtils.generateOTPCode();
        otpdto.setOtpCode(otpCode);
        otpdto.setUsed(false);
        otpdto.setCreatedAt(LocalDateTime.now());
        otpdto.setExpiredAt(LocalDateTime.now().plusMinutes(2));
        otpRepo.save(otpMapper.toEntity(otpdto));
        mailService.sendMail(dto.getEmail(), MailConstant.SUBJECT, MailConstant.CONTENT + otpCode);
    }
}
