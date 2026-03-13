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

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);
    private final ContractRepository contractRepo;
    private final SavingBookRepository savingBookRepo;
    private final ContractMapper contractMapper;
    private final ContractServiceHelper contractHelper;

    public CommonResponse createContract(ContractDTO dto) {
        log.info("[Begin]Create contract with data request: {}", dto);
        contractHelper.checkSavingBookIsValid(dto);
        log.info("The saving book exists and is valid with id: {}", dto.getSavingBookId());
        ContractCodeDTO contractCode = contractMapper.createContractCode(dto);
        String generateCode = CommonUtils.generateContractCode(contractCode);
        dto.setContractCode(generateCode);
        dto.setStatus(CommonConstant.PENDING_CONTRACT);
        dto.setCreatedAt(LocalDate.now());
        contractRepo.save(contractMapper.toEntity(dto));
        contractHelper.sendAndSaveOTP(dto);
        log.info("Contract is created with code: {}", generateCode);
        return CommonResponse.success();
    }
    public CommonResponse signContract(String OtpCode, String contractCode) {
        log.info("[Begin] SignContract with contractCode: {}", contractCode);
        ContractDTO contractDTO = contractMapper.toDTO(contractRepo.findByContractCode(contractCode));
        if (contractDTO == null) {
            throw new RuntimeException("Contract not found with code: " + contractCode);
        }
        if (contractDTO.getStatus() != CommonConstant.PENDING_CONTRACT) {
            throw new RuntimeException("Contract is not in pending status");
        }
        contractHelper.verifyOTP(contractCode, OtpCode);
        contractDTO.setStatus(CommonConstant.SIGNED_CONTRACT);
        contractRepo.save(contractMapper.toEntity(contractDTO));
        SavingBook savingBook = savingBookRepo.findById(contractDTO.getSavingBookId());
        savingBook.setStatus(CommonConstant.LOCKED_SAVING_BOOK);
        savingBookRepo.save(savingBook);
        log.info("[End] Sign contract successfully with contractCode: {}", contractCode);
        return CommonResponse.success();
    }

    public CommonResponse deleteContract(String contractCode) {
        log.info("[Begin] Delete contract with contractCode: {}", contractCode);
        ContractDTO contractDTO = contractMapper.toDTO(contractRepo.findByContractCode(contractCode));
        if (contractDTO == null) {
            throw new RuntimeException("Contract not found with code: " + contractCode);
        }
        if (contractDTO.getStatus() == CommonConstant.SIGNED_CONTRACT
                || contractDTO.getStatus() == CommonConstant.ACCEPT_CONTRACT
                || contractDTO.getStatus() == CommonConstant.REJECTED_CONTRACT) {
            throw new RuntimeException("Contract is already signed, cannot delete");
        }
        contractDTO.setStatus(CommonConstant.DELETED_CONTRACT);
        contractRepo.save(contractMapper.toEntity(contractDTO));
        log.info("[End] Delete contract successfully with contractCode: {}", contractCode);
        return CommonResponse.success();
    }
}
