package com.agribank.e_contract.mapper;

import com.agribank.e_contract.dto.ContractCodeDTO;
import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.entity.Contract;
import com.agribank.e_contract.entity.OTP;
import com.agribank.e_contract.repository.BranchRepository;
import com.agribank.e_contract.repository.ClientRepository;
import com.agribank.e_contract.repository.ContractRepository;
import com.agribank.e_contract.repository.SavingBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractMapper {
    public final ContractRepository contractRepo;
    public final ClientRepository clientRepo;
    public final BranchRepository branchRepo;
    public final SavingBookRepository savingBookRepo;

    public ContractDTO toDTO(Contract contract) {
        if (contract == null) {
            return null;
        }
        ContractDTO dto = new ContractDTO();
        dto.setContractCode(contract.getContractCode());
        if (contract.getClient() != null) {
            dto.setClientId(contract.getClient().getId());
        }
        if (contract.getBranch() != null) {
            dto.setBranchId(contract.getBranch().getId());
        }
        if (contract.getSavingBook() != null) {
            dto.setSavingBookId(contract.getSavingBook().getId());
        }
        dto.setEmail(contract.getEmail());
        dto.setLoanAmount(contract.getLoanAmount());
        dto.setStatus(contract.getStatus());
        dto.setLoanTerm(contract.getLoanTerm());
        dto.setInterestRate(contract.getInterestRate());
        dto.setCreatedAt(contract.getCreatedAt());
        return dto;
    }

    public Contract toEntity(ContractDTO dto) {
        if (dto == null) {
            return null;
        }
        Contract contract = new Contract();
        contract.setContractCode(dto.getContractCode());
        if (dto.getClientId() != 0) {
            contract.setClient(clientRepo.findClientById(dto.getClientId()));
        }
        if (dto.getBranchId() != 0) {
            contract.setBranch(branchRepo.findById(dto.getBranchId()).orElseThrow(
                    () -> new RuntimeException("Branch not found with id: " + dto.getBranchId())
            ));
        }
        if (dto.getSavingBookId() != 0) {
            contract.setSavingBook(savingBookRepo.findById(dto.getSavingBookId()));
        }
        contract.setEmail(dto.getEmail());
        contract.setLoanAmount(dto.getLoanAmount());
        contract.setLoanTerm(dto.getLoanTerm());
        contract.setStatus(dto.getStatus());
        contract.setInterestRate(dto.getInterestRate());
        contract.setCreatedAt(dto.getCreatedAt());
        return contract;
    }

    public ContractCodeDTO createContractCode(ContractDTO contractDTO) {
        if (contractDTO == null) {
            return null;
        }
        ContractCodeDTO dto = new ContractCodeDTO();
        dto.setClientId(contractDTO.getClientId());
        dto.setBranchId(contractDTO.getBranchId());
        dto.setSavingBookId(contractDTO.getSavingBookId());
        dto.setCreatedAt(contractDTO.getCreatedAt());
        return dto;
    }
}
