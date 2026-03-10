package com.agribank.e_contract.utils;

import com.agribank.e_contract.dto.ContractCodeDTO;

import java.security.SecureRandom;

public class CommonUtils {
    public static String generateContractCode(ContractCodeDTO dto) {
        int clientId = dto.getClientId();
        int branchId = dto.getBranchId();
        int savingBookId = dto.getSavingBookId();
        return String.format("CT%02d%02d%02d", clientId, branchId, savingBookId);
    }
    public static String generateOTPCode() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }
}
