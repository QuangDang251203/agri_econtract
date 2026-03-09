package com.agribank.e_contract.mapper;

import com.agribank.e_contract.dto.BranchDTO;
import com.agribank.e_contract.entity.Branch;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {
    public Branch toEntity(BranchDTO dto) {
        Branch branch = new Branch();
        branch.setId(dto.getId());
        branch.setBranchName(dto.getBranchName());
        branch.setInterestRate(dto.getInterestRate());
        return branch;
    }
    public BranchDTO toDto(Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setId(branch.getId());
        dto.setBranchName(branch.getBranchName());
        dto.setInterestRate(branch.getInterestRate());
        return dto;
    }
}
