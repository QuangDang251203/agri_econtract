package com.agribank.e_contract.service.contract;

import com.agribank.e_contract.constant.CommonConstant;
import com.agribank.e_contract.constant.MailConstant;
import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.ContractRequest;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ContractServiceHelper {
    public final Logger log = LoggerFactory.getLogger(ContractServiceHelper.class);
    private final SavingBookRepository savingBookRepo;
    private final RedisTemplate<String, String> redisTemplate;
    private final ClientRepository clientRepo;

    public void SaveOTP(ContractDTO dto) {
        String otp = CommonUtils.generateOTPCode();
        String key = "otp:contract:" + dto.getContractCode();
        redisTemplate.opsForValue().set(
                key,
                otp,
                30,
                TimeUnit.MINUTES
        );

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

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Map<String, Object> buildTemplateData(ContractRequest request) {
        LocalDate currentDate = LocalDate.now();

        Map<String, Object> data = new HashMap<>();
        data.put("ten_ben_vay", request.getBusinessName());
        data.put("dia_chi", request.getAddress());
        data.put("so_dien_thoai", request.getPhoneNumber());
        data.put("ma_so_doanh_nghiep", request.getBusinessCode());
        data.put("so_tai_khoan", request.getBankAccountNumber());
        data.put("ngay_lap_hop_dong", formatDate(currentDate));
        data.put("ten_chi_nhanh", request.getBranchName());
        data.put("ten_nguoi_dai_dien_ben_vay", request.getRepresentative());
        data.put("chuc_vu_nguoi_dai_dien_ben_vay", "Giám đốc");
        data.put("so_CCCD", request.getCccdNumber());
        data.put("noi_cap_CCCD", request.getIssuingLocation());
        data.put("ngay_cap_CCCD", formatDate(request.getDateIssued()));
        data.put("so_tien_vay", request.getLoanAmount().stripTrailingZeros().toPlainString());
        data.put("so_tien_vay_bang_chu", request.getLoanAmountInWords());
        data.put("thoi_han_vay", request.getLoanTerm());
        data.put("lai_suat", request.getInterestRate());
        data.put("seri_so_tiet_kiem", request.getSavingBookId());
        data.put("so_tien_gui_tiet_kiem", request.getBalance().stripTrailingZeros().toPlainString());
        data.put("so_tien_gui_tiet_kiem_bang_chu", request.getBalenceInWords());
        data.put("ngay_phat_hanh", formatDate(request.getDateOfDeposit()));
        data.put("ngay_den_han", formatDate(calculateMaturityDate(request)));
        return data;
    }

    public LocalDate calculateMaturityDate(ContractRequest request) {
        if (request.getDateOfDeposit() == null) {
            return null;
        }
        return request.getDateOfDeposit().plusMonths(request.getDuration());
    }

    public String formatDate(LocalDate date) {
        return date == null ? "" : date.format(FORMATTER);
    }

    public String getSavePath() {
        return "C:/Users/Hi/OneDrive - utt.vn/CÔNG VIỆC/Template_demo/";
    }

    public String getOTP(String contractCode) {
        String key = "otp:contract:" + contractCode;
        return (String) redisTemplate.opsForValue().get(key);
    }
}
