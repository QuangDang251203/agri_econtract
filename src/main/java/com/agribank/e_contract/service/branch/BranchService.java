package com.agribank.e_contract.service.branch;

import com.agribank.e_contract.entity.Branch;
import com.agribank.e_contract.response.CommonResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BranchService {
    List<Branch> getAllBranches();
    CommonResponse updateInterestRate(int id, float interestRate);
}
