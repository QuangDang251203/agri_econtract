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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    public final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);
    public final ContractRepository contractRepo;
    public final SavingBookRepository savingBookRepo;
    public final ContractMapper contractMapper;
    public final ContractServiceHelper contractHelper;
    public final StringRedisTemplate redisTemplate;

    public CommonResponse createContract(ContractDTO dto) {
        log.info("[Begin]Create contract with data request: {}", dto);
        SavingBook savingBook = savingBookRepo.findById(dto.getSavingBookId());
        if (savingBook.getClient().getId() != dto.getClientId()) {
            throw new RuntimeException("Saving book does not belong to this client");
        }
        if (savingBook.getStatus() == CommonConstant.LOCKED_SAVING_BOOK) {
            throw new RuntimeException("Saving book locked");
        }
        log.info("The saving book exists and the borrower is the correct person");
        ContractCodeDTO contractCode = contractMapper.createContractCode(dto);
        String generateCode = CommonUtils.generateContractCode(contractCode);
        dto.setContractCode(generateCode);
        dto.setStatus(CommonConstant.PENDING_CONTRACT);
        contractRepo.save(contractMapper.toEntity(dto));
        contractHelper.sendAndSaveOTP(dto);
        log.info("Contract is created with code: {}", generateCode);
        return CommonResponse.success();
    }

    @Transactional
    public CommonResponse updateStatusFromManager(UpdateContractDTO dto) {
        log.info("[Begin]Update status from Manager with code contract: {}", dto.getContractCode());
        ContractDTO contractDTO = contractMapper.toDTO(contractRepo.findByContractCode(dto.getContractCode()));
        if (contractDTO == null) {
            throw new RuntimeException("Contract not found with code: " + dto.getContractCode());
        }
        log.info("Contract is exist with code: {}", dto.getContractCode());

        switch (contractDTO.getStatus()) {
            case CommonConstant.PENDING_CONTRACT:
                throw new RuntimeException("Contract is still pending, waiting for borrower to sign");
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


}
