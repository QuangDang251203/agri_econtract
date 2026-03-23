package com.agribank.e_contract.dto;

import com.agribank.e_contract.entity.CCCD;
import com.agribank.e_contract.entity.Contract;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllContractDTO {
    private Contract contractInfo;
    private CCCD cccdInfo;
}
