package com.agribank.e_contract.service.contract;

import com.agribank.e_contract.constant.CommonConstant;
import com.agribank.e_contract.dto.ContractCodeDTO;
import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.UpdateContractDTO;
import com.agribank.e_contract.entity.SavingBook;
import com.agribank.e_contract.mapper.ContractMapper;
import com.agribank.e_contract.repository.ContractRepository;
import com.agribank.e_contract.repository.SavingBookRepository;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.utils.CommonUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    public final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);
    public final ContractRepository contractRepo;
    public final SavingBookRepository savingBookRepo;
    public final ContractMapper contractMapper;

    public CommonResponse createContract(ContractDTO dto) {
        log.info("[Begin]Create contract with data request: {}", dto);
        SavingBook savingBook = savingBookRepo.findById(dto.getSavingBookId());
        if (savingBook.getClient().getId() != dto.getClientId()) {
            throw new RuntimeException("Saving book does not belong to this client");
        }
        log.info("The saving book exists and the borrower is the correct person");
        ContractCodeDTO contractCode = contractMapper.createContractCode(dto);
        String generateCode = CommonUtils.generateContractCode(contractCode);
        dto.setContractCode(generateCode);
        dto.setStatus(CommonConstant.PENDING_CONTRACT);
        contractRepo.save(contractMapper.toEntity(dto));
        log.info("Contract is created with code: {}", generateCode);
        return CommonResponse.success();
    }

    @Transactional
    public CommonResponse updateStatus(UpdateContractDTO dto) {
        log.info("[Begin]Contract signed with code: {}", dto.getContractCode());
        ContractDTO contractDTO = contractMapper.toDTO(contractRepo.findByContractCode(dto.getContractCode()));
        if (contractDTO == null) {
            throw new RuntimeException("Contract not found with code: " + dto.getContractCode());
        }
        log.info("Contract is exist with code: {}", dto.getContractCode());

        switch (contractDTO.getStatus()) {
            case CommonConstant.PENDING_CONTRACT:
                if (dto.getStatus() == CommonConstant.SIGNED_CONTRACT) {
                    contractDTO.setStatus(CommonConstant.SIGNED_CONTRACT);
                } else if (dto.getStatus() == CommonConstant.EXPIRED_CONTRACT) {
                    contractDTO.setStatus(CommonConstant.EXPIRED_CONTRACT);
                } else {
                    contractDTO.setStatus(CommonConstant.DELETED_CONTRACT);
                }
                break;
            case CommonConstant.SIGNED_CONTRACT:
                if (dto.getStatus() == CommonConstant.ACCEPT_CONTRACT) {
                    contractDTO.setStatus(CommonConstant.ACCEPT_CONTRACT);
                } else {
                    contractDTO.setStatus(CommonConstant.REJECTED_CONTRACT);
                }
                break;
            default:
                throw new RuntimeException("Contract is already in final status");
        }
        log.info("Contract is updated to status: {}", contractDTO.getStatus());
        contractRepo.save(contractMapper.toEntity(contractDTO));
        return CommonResponse.success();
    }


}
