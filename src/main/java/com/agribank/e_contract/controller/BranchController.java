package com.agribank.e_contract.controller;

import com.agribank.e_contract.dto.UpdateInterestRateDTO;
import com.agribank.e_contract.entity.Branch;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.response.ResponseList;
import com.agribank.e_contract.service.branch.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/branch")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;
    @GetMapping("/getAllBranch")
    public ResponseList<Branch> getAllBranch() {
        List<Branch> branches = branchService.getAllBranches();
        return ResponseList.success(branches);
    }

    @PutMapping("/updateInterestRate/{id}")
    public CommonResponse updateInterestRate (@PathVariable int id,
                                              @Valid @RequestBody UpdateInterestRateDTO dto) {
        return branchService.updateInterestRate(id, dto.getInterestRate());
    }
}
