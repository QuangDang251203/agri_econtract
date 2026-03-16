package com.agribank.e_contract.service.contract;

import com.agribank.e_contract.constant.CommonConstant;
import com.agribank.e_contract.dto.ContractCodeDTO;
import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.ContractRequest;
import com.agribank.e_contract.entity.BankAccount;
import com.agribank.e_contract.entity.Client;
import com.agribank.e_contract.entity.Contract;
import com.agribank.e_contract.entity.SavingBook;
import com.agribank.e_contract.mapper.ContractMapper;
import com.agribank.e_contract.repository.BankAccountRepository;
import com.agribank.e_contract.repository.ClientRepository;
import com.agribank.e_contract.repository.ContractRepository;
import com.agribank.e_contract.repository.SavingBookRepository;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.utils.CommonUtils;
import com.deepoove.poi.XWPFTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);
    private final ContractRepository contractRepo;
    private final SavingBookRepository savingBookRepo;
    private final ContractMapper contractMapper;
    private final ContractServiceHelper contractHelper;
    private final BankAccountRepository bankAccountRepository;
    private final ClientRepository clientRepository;

    public String createContract(ContractDTO dto) {
        log.info("[Begin]Create contract with data request: {}", dto);
        contractHelper.checkSavingBookIsValid(dto);
        log.info("The saving book exists and is valid with id: {}", dto.getSavingBookId());
        ContractCodeDTO contractCode = contractMapper.createContractCode(dto);
        String generateCode = CommonUtils.generateContractCode(contractCode);
        dto.setContractCode(generateCode);
        dto.setStatus(CommonConstant.PENDING_CONTRACT);
        dto.setCreatedAt(LocalDate.now());
        contractRepo.save(contractMapper.toEntity(dto));
        contractHelper.SaveOTP(dto);
        log.info("Contract is created with code: {}", generateCode);
        return generateCode;
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

    public ResponseEntity<byte[]> generateAndDownloadContract(String contractCode) throws IOException {
        Contract contract = contractRepo.findByContractCode(contractCode);
        if (contract == null) {
            throw new RuntimeException("Contract not found with code: " + contractCode);
        }

        BankAccount bankAccount = bankAccountRepository.findById(contract.getBankAccount().getId());
        SavingBook savingBook = savingBookRepo.findById(contract.getSavingBook().getId());
        Client client = clientRepository.findClientByBusinessCode(contract.getClient().getBusinessCode());
        if (client == null) {
            throw new RuntimeException("Client not found with business code: " + contract.getClient().getBusinessCode());
        }

        ContractRequest request = contractMapper.toContractRequest(contract, client, savingBook, bankAccount);
        Map<String, Object> data = contractHelper.buildTemplateData(request);

        ClassPathResource resource = new ClassPathResource("templates/Template_hop_dong_vay.docx");
        String savePath = contractHelper.getSavePath();
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String baseFileName = "hop_dong_" + System.currentTimeMillis();
        File docxFile = new File(savePath + baseFileName + ".docx");
        File pdfFile = new File(savePath + baseFileName + ".pdf");

        try {
            // 1) Render template ra DOCX
            try (FileOutputStream fos = new FileOutputStream(docxFile)) {
                XWPFTemplate template = XWPFTemplate.compile(resource.getInputStream()).render(data);
                template.write(fos);
                template.close();
            }

            // 2) Convert DOCX -> PDF
            convertDocxToPdf(docxFile, dir);

            if (!pdfFile.exists()) {
                throw new IOException("PDF conversion failed: output file not found");
            }

            // 3) Đọc PDF và trả về
            byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName())
                    .header("contractCode", contractCode)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } finally {
            // Nếu muốn giữ file trên server thì bỏ phần xóa
            if (docxFile.exists()) {
                docxFile.delete();
            }
            // Nếu muốn giữ PDF để chèn chữ ký sau này thì KHÔNG xóa pdfFile
            // Nếu chỉ download xong là xóa thì mở dòng dưới:
            // if (pdfFile.exists()) pdfFile.delete();
        }
    }

    private void convertDocxToPdf(File docxFile, File outputDir) throws IOException {
        // Đổi path này theo máy bạn
        String sofficePath = "C:/Program Files/LibreOffice/program/soffice.exe";

        ProcessBuilder processBuilder = new ProcessBuilder(
                sofficePath,
                "--headless",
                "--convert-to", "pdf",
                "--outdir", outputDir.getAbsolutePath(),
                docxFile.getAbsolutePath()
        );

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        String output;
        try (InputStream is = process.getInputStream()) {
            output = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }

        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Convert DOCX to PDF failed. Exit code: " + exitCode + "\nLog: " + output);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("DOCX to PDF conversion interrupted", e);
        }
    }

    public String moneyToWords(long amount) {
        return CommonUtils.numberToWords(amount);
    }

    public String SendOTP(String contractCode) {
        ContractDTO contractDTO = contractMapper.toDTO(contractRepo.findByContractCode(contractCode));
        if (contractDTO == null) {
            throw new RuntimeException("Contract not found with code: " + contractCode);
        }
        String otp = contractHelper.getOTP(contractCode);
        if (contractDTO.getStatus() != CommonConstant.PENDING_CONTRACT) {
            throw new RuntimeException("Contract is not in pending status");
        }
        return "OTP code for contract " + contractCode + " is: " + otp;
    }
}
