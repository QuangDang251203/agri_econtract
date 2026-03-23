package com.agribank.e_contract.mapper;

import com.agribank.e_contract.dto.*;
import com.agribank.e_contract.entity.BankAccount;
import com.agribank.e_contract.entity.Client;
import com.agribank.e_contract.entity.Contract;
import com.agribank.e_contract.entity.SavingBook;
import com.agribank.e_contract.repository.BankAccountRepository;
import com.agribank.e_contract.repository.ClientRepository;
import com.agribank.e_contract.repository.SavingBookRepository;
import com.agribank.e_contract.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractMapper {
    public final ClientRepository clientRepo;
    public final SavingBookRepository savingBookRepo;
    public final BankAccountRepository bankAccountRepo;

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
        if (contract.getBankAccount() != null) {
            dto.setBankAccountId(contract.getBankAccount().getId());
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
        if (dto.getBankAccountId() != 0) {
            contract.setBankAccount(bankAccountRepo.findById(dto.getBankAccountId()));
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

    public ContractRequest toContractRequest(Contract contract,
                                             Client client,
                                             SavingBook savingBook,
                                             BankAccount bankAccount) {
        ContractRequest request = new ContractRequest();
        request.setSavingBookId(contract.getSavingBook().getId());
        request.setLoanAmount(contract.getLoanAmount());
        request.setLoanTerm(contract.getLoanTerm());
        request.setLoanAmountInWords(CommonUtils.numberToWords(contract.getLoanAmount().longValue()));
        request.setInterestRate((contract.getInterestRate())* 100);
        request.setBusinessName(client.getBusinessName());
        request.setAddress(client.getAddress());
        request.setPhoneNumber(client.getPhone());
        request.setBusinessCode(client.getBusinessCode());
        request.setBankAccountNumber(bankAccount.getBankAccountNumber());
        request.setBranchName(bankAccount.getBranchName());
        request.setRepresentative(client.getCccd().getRepresentative());
        request.setCccdNumber(client.getCccd().getCccdNumber());
        request.setIssuingLocation(client.getCccd().getIssuingLocation());
        request.setDateIssued(client.getCccd().getDateIssued());
        request.setSavingBookId(savingBook.getId());
        request.setBalance(savingBook.getBalance());
        request.setBalenceInWords(CommonUtils.numberToWords(savingBook.getBalance().longValue()));
        request.setDateOfDeposit(savingBook.getCreatedAt());
        request.setDuration(savingBook.getDuration());

        return request;
    }
}
