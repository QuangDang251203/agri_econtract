package com.agribank.e_contract.service.contract;

import com.agribank.e_contract.constant.MailConstant;
import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.service.mail.MailService;
import com.agribank.e_contract.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ContractServiceHelper {
    public final Logger log = LoggerFactory.getLogger(ContractServiceHelper.class);
    public final MailService mailService;
    public final RedisTemplate<String, String> redisTemplate;

    public void sendAndSaveOTP(ContractDTO dto) {
        String otp = CommonUtils.generateOTPCode();
        String key = "otp:contract:" + dto.getContractCode();
        redisTemplate.opsForValue().set(
                key,
                otp,
                2,
                TimeUnit.MINUTES
        );
        mailService.sendMail(dto.getEmail(), MailConstant.SUBJECT, MailConstant.CONTENT + otp);
    }

    public void verifyOTP(String contractCode, String otpCode) {
        log.info("[Begin] Verify OTP with ContractCode: {}", contractCode);
        String key = "otp:contract:" + contractCode;
        String redisOtp = redisTemplate.opsForValue().get(key);
        if (redisOtp == null) {
            throw new RuntimeException("OTP expired or not found");
        }
        if (!redisOtp.equals(otpCode)) {
            throw new RuntimeException("OTP is incorrect");
        }
        redisTemplate.delete(key);
    }
}
