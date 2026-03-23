package com.agribank.e_contract.mapper;

import com.agribank.e_contract.dto.FileContractDTO;
import com.agribank.e_contract.entity.FileContract;
import com.agribank.e_contract.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileContractMapper {
    private final ContractRepository contractRepo;
    public FileContract toEntity(FileContractDTO dto) {
        if (dto == null) {
            return null;
        }
        FileContract fileContract = new FileContract();
//        fileContract.setId(dto.getId());
        fileContract.setContract(contractRepo.findByContractCode(dto.getContractCode()));
        fileContract.setFilePath(dto.getFilePath());
        fileContract.setFileType(dto.getFileType());
        return fileContract;
    }
    public FileContractDTO toDTO(FileContract fileContract) {
        if (fileContract == null) {
            return null;
        }
        FileContractDTO dto = new FileContractDTO();
        dto.setId(fileContract.getId());
        dto.setFilePath(fileContract.getFilePath());
        dto.setFileType(fileContract.getFileType());
        if (fileContract.getContract() != null) {
            dto.setContractCode(fileContract.getContract().getContractCode());
        }
        dto.setCreatedAt(fileContract.getCreatedAt());
        return dto;
    }
}
