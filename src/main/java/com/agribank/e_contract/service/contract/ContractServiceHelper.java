package com.agribank.e_contract.service.contract;

import com.agribank.e_contract.constant.CommonConstant;
import com.agribank.e_contract.constant.MailConstant;
import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.entity.SavingBook;
import com.agribank.e_contract.repository.ClientRepository;
import com.agribank.e_contract.repository.SavingBookRepository;
import com.agribank.e_contract.service.mail.MailService;
import com.agribank.e_contract.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ContractServiceHelper {
    public final Logger log = LoggerFactory.getLogger(ContractServiceHelper.class);
    private final MailService mailService;
    private final SavingBookRepository savingBookRepo;
    private final RedisTemplate<String, String> redisTemplate;
    private final ClientRepository clientRepo;

    public void sendAndSaveOTP(ContractDTO dto) {
        String otp = CommonUtils.generateOTPCode();
        String key = "otp:contract:" + dto.getContractCode();
        redisTemplate.opsForValue().set(
                key,
                otp,
                2,
                TimeUnit.MINUTES
        );
        mailService.sendMail(clientRepo.findClientByBusinessCode(dto.getBusinessCode()).getEmail(),
                MailConstant.SUBJECT, MailConstant.CONTENT + otp);
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

    public void checkSavingBookIsValid(ContractDTO dto) {
        log.info("[Begin] Check saving book of contract: {}", dto.getContractCode());
        SavingBook savingBook = savingBookRepo.findById(dto.getSavingBookId());
        if (!savingBook.getClient().getBusinessCode().equals(dto.getBusinessCode())) {
            throw new RuntimeException("Saving book does not belong to this client");
        }
        if (savingBook.getStatus() == CommonConstant.LOCKED_SAVING_BOOK) {
            throw new RuntimeException("Saving book locked");
        }
        BigDecimal limit = savingBook.getBalance()
                .multiply(BigDecimal.valueOf(90))
                .divide(BigDecimal.valueOf(100));

        if (dto.getLoanAmount().compareTo(limit) > 0) {
            throw new RuntimeException("Loan amount must be less than 90% of saving book balance");
        }
    }
}
