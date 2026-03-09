package com.agribank.e_contract.service.branch;

import com.agribank.e_contract.dto.BranchDTO;
import com.agribank.e_contract.entity.Branch;
import com.agribank.e_contract.mapper.BranchMapper;
import com.agribank.e_contract.repository.BranchRepository;
import com.agribank.e_contract.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final Logger log = LoggerFactory.getLogger(BranchServiceImpl.class);
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public List<Branch> getAllBranches() {
        log.info("[Begin]Get all branches");
        return branchRepository.findAll();
    }

    public CommonResponse updateInterestRate(int id, float interestRate) {
        log.info("[Begin]Update branchID {} interest rate with value: {}", id, interestRate);
        Branch branch = branchRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Branch not found")
        );
        BranchDTO branchDTO = branchMapper.toDto(branch);
        branchDTO.setInterestRate(interestRate);
        branchRepository.save(branchMapper.toEntity(branchDTO));
        return CommonResponse.success();
    }



}
