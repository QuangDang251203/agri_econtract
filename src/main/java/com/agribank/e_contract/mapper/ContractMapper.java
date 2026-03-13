package com.agribank.e_contract.mapper;

import com.agribank.e_contract.dto.ContractCodeDTO;
import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.entity.Contract;
import com.agribank.e_contract.repository.ClientRepository;
import com.agribank.e_contract.repository.ContractRepository;
import com.agribank.e_contract.repository.SavingBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractMapper {
    public final ClientRepository clientRepo;
    public final SavingBookRepository savingBookRepo;

    public ContractDTO toDTO(Contract contract) {
        if (contract == null) {
            return null;
        }
        ContractDTO dto = new ContractDTO();
        dto.setContractCode(contract.getContractCode());
        if (contract.getClient() != null) {
            dto.setBusinessCode(contract.getClient().getBusinessCode());
        }
        if (contract.getSavingBook() != null) {
            dto.setSavingBookId(contract.getSavingBook().getId());
        }
        dto.setLoanAmount(contract.getLoanAmount());
        dto.setStatus(contract.getStatus());
        dto.setLoanTerm(contract.getLoanTerm());
        dto.setPaymentMethod(contract.getPaymentMethod());
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
        if (dto.getBusinessCode() != null) {
            contract.setClient(clientRepo.findClientByBusinessCode(dto.getBusinessCode()));
        }
        if (dto.getSavingBookId() != 0) {
            contract.setSavingBook(savingBookRepo.findById(dto.getSavingBookId()));
        }
        contract.setLoanAmount(dto.getLoanAmount());
        contract.setLoanTerm(dto.getLoanTerm());
        contract.setStatus(dto.getStatus());
        contract.setPaymentMethod(dto.getPaymentMethod());
        contract.setInterestRate(dto.getInterestRate());
        contract.setCreatedAt(dto.getCreatedAt());
        return contract;
    }

    public ContractCodeDTO createContractCode(ContractDTO contractDTO) {
        if (contractDTO == null) {
            return null;
        }
        ContractCodeDTO dto = new ContractCodeDTO();
        dto.setBusinessCode(contractDTO.getBusinessCode());
        dto.setSavingBookId(contractDTO.getSavingBookId());
        dto.setCreatedAt(contractDTO.getCreatedAt());
        return dto;
    }
}
