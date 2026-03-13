package com.agribank.e_contract.utils;

import com.agribank.e_contract.dto.ContractCodeDTO;

import java.security.SecureRandom;

public class CommonUtils {
    public static String generateContractCode(ContractCodeDTO dto) {
        String businessCode = dto.getBusinessCode();
        int savingBookId = dto.getSavingBookId();

        String businessPart = businessCode.substring(0, 4);
        String savingPart = String.format("%02d", savingBookId);

        return "CT" + businessPart + savingPart;
    }
    public static String generateOTPCode() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }
}
