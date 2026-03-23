package com.agribank.e_contract.dto;

import com.agribank.e_contract.entity.CCCD;
import com.agribank.e_contract.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDetailDTO {
    private ContractDTO contractInfo;
    private FileContractDTO fileInfo;
    private Client clientInfo;
    private CCCD cccdInfo;
    private String fileName;
    private String mimeType;
    private String fileContentBase64;
}

