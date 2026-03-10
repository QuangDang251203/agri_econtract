package com.agribank.e_contract.utils;

import com.agribank.e_contract.dto.ContractCodeDTO;

public class CommonUtils {
    public static String generateContractCode(ContractCodeDTO dto) {
        int clientId = dto.getClientId();
        int branchId = dto.getBranchId();
        int savingBookId = dto.getSavingBookId();
        return String.format("CT%02d%02d%02d", clientId, branchId, savingBookId);
    }
}
