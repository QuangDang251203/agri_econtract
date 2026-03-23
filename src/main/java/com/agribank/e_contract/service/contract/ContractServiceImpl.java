package com.agribank.e_contract.service.contract;

import com.agribank.e_contract.constant.CommonConstant;
import com.agribank.e_contract.dto.*;
import com.agribank.e_contract.entity.*;
import com.agribank.e_contract.mapper.ContractMapper;
import com.agribank.e_contract.mapper.FileContractMapper;
import com.agribank.e_contract.repository.BankAccountRepository;
import com.agribank.e_contract.repository.ClientRepository;
import com.agribank.e_contract.repository.ContractRepository;
import com.agribank.e_contract.repository.FileContractRepository;
import com.agribank.e_contract.repository.SavingBookRepository;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.response.ResponseList;
import com.agribank.e_contract.response.ResponseObject;
import com.agribank.e_contract.utils.CommonUtils;
import com.deepoove.poi.XWPFTemplate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;
import java.util.List;
import java.util.Optional;


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
    private final FileContractRepository fileContractRepository;
    private final FileContractMapper filePathMapper;

    public String createContract(ContractDTO dto) {
        log.info("[Begin]Create contract with data request: {}", dto);
        contractHelper.checkSavingBookIsValid(dto);
        log.info("The saving book exists and is valid with id: {}", dto.getSavingBookId());
        ContractCodeDTO contractCode = contractMapper.createContractCode(dto);
        String generateCode = CommonUtils.generateContractCode();
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

            // Persist generated file metadata so downstream features can track contract files.
            FileContractDTO fileContractDTO = new FileContractDTO();
            fileContractDTO.setContractCode(contractCode);
            fileContractDTO.setFilePath(pdfFile.getAbsolutePath());
            fileContractDTO.setFileType("pdf");
            fileContractRepository.save(filePathMapper.toEntity(fileContractDTO));

            // 3) Đọc PDF và trả về
            byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName())
                    .header("contractCode", contractCode)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } finally {
            if (docxFile.exists()) {
                docxFile.delete();
            }
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

    public ResponseList<Contract> getContractByBusinessCode(String businessCode) {
        log.info("[Begin] Get contract by business code: {}", businessCode);
        List<Contract> contracts = contractRepo.findByClientBusinessCode(businessCode);
        log.info("[End] Get {} contracts by business code: {}", contracts.size(), businessCode);
        return ResponseList.success(contracts);
    }

    public ResponseList<AllContractDTO> getAllContracts() {
        log.info("[Begin] Get all contracts");
        List<Contract> contracts = contractRepo.findAll();
        log.info("[End] Get {} contracts", contracts.size());

        List<AllContractDTO> data = new java.util.ArrayList<>();
        for (Contract contract : contracts) {
            CCCD cccd = null;
            if (contract.getClient() != null) {
                cccd = contract.getClient().getCccd();
            }
            data.add(new AllContractDTO(contract, cccd));
        }

        return ResponseList.success(data);
    }

    public ResponseObject<ContractDetailDTO> getContractDetailByContractCode(String contractCode) throws IOException {
        log.info("[Begin] Get contract detail by contractCode: {}", contractCode);

        Contract contract = contractRepo.findByContractCode(contractCode);
        if (contract == null) {
            throw new RuntimeException("Contract not found with code: " + contractCode);
        }
        Client client = clientRepository.findClientByBusinessCode(contract.getClient().getBusinessCode());
        if (client == null) {
            throw new RuntimeException("Client not found with business code: " + contract.getClient().getBusinessCode());
        }
        CCCD cccd = client.getCccd();
        if (cccd == null) {
            throw new RuntimeException("CCCD not found for client with business code: " + client.getBusinessCode());
        }
        FileContract fileContract = fileContractRepository.findFileByPriority(contractCode)
                .orElseThrow(() -> new RuntimeException("No contract file found for contract code: " + contractCode));

        Resource resource = getContractFileResource(contractCode);
        byte[] fileBytes;
        try (InputStream inputStream = resource.getInputStream()) {
            fileBytes = inputStream.readAllBytes();
        }

        Path filePath = Paths.get(fileContract.getFilePath());
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = MediaType.APPLICATION_PDF_VALUE;
        }

        FileContractDTO fileInfo = new FileContractDTO(
                fileContract.getId(),
                fileContract.getFilePath(),
                fileContract.getFileType(),
                contractCode,
                fileContract.getCreatedAt()
        );

        ContractDetailDTO contractDetailDTO = new ContractDetailDTO();
        contractDetailDTO.setContractInfo(contractMapper.toDTO(contract));
        contractDetailDTO.setFileInfo(fileInfo);
        contractDetailDTO.setClientInfo(client);
        contractDetailDTO.setCccdInfo(cccd);
        contractDetailDTO.setFileName(filePath.getFileName().toString());
        contractDetailDTO.setMimeType(mimeType);
        contractDetailDTO.setFileContentBase64(Base64.getEncoder().encodeToString(fileBytes));

        log.info("[End] Get contract detail by contractCode successfully: {}", contractCode);
        return ResponseObject.success(contractDetailDTO);
    }

    @Transactional
    public CommonResponse signContractWithSignature(String contractCode,
                                                    String otpCode,
                                                    MultipartFile signatureFile) throws IOException {
        log.info("[Begin] Sign contract with signature - contractCode: {}", contractCode);

        Contract contract = contractRepo.findByContractCode(contractCode);
        if (contract == null) {
            throw new RuntimeException("Contract not found with code: " + contractCode);
        }

        if (contract.getStatus() != CommonConstant.PENDING_CONTRACT) {
            throw new RuntimeException("Contract is not in pending status");
        }

        contractHelper.verifyOTP(contractCode, otpCode);

        Optional<FileContract> latestPdfOpt =
                fileContractRepository.findTopByContract_ContractCodeAndFileTypeOrderByIdDesc(contractCode, "pdf");

        FileContract latestPdf = latestPdfOpt
                .orElseThrow(() -> new RuntimeException("Pending PDF not found for contract: " + contractCode));

        byte[] signatureBytes = signatureFile.getBytes();

        String signedPdfPath = contractHelper.buildSignedPdfPath(latestPdf.getFilePath());
        contractHelper.stampSignatureOnPdf(latestPdf.getFilePath(), signatureBytes, signedPdfPath);

        FileContract signedFile = new FileContract();
        signedFile.setContract(contract);
        signedFile.setFilePath(signedPdfPath);
        signedFile.setFileType("signed_pdf");
        fileContractRepository.save(signedFile);

        contract.setStatus(CommonConstant.SIGNED_CONTRACT);
        contractRepo.save(contract);

        SavingBook savingBook = savingBookRepo.findById(contract.getSavingBook().getId());
        savingBook.setStatus(CommonConstant.LOCKED_SAVING_BOOK);
        savingBookRepo.save(savingBook);

        log.info("[End] Sign contract successfully - contractCode: {}", contractCode);
        return CommonResponse.success();
    }

    public Resource getContractFileResource(String contractCode) throws MalformedURLException {
        log.info("[Begin] Get contract file resource by contract code: {}", contractCode);

        FileContract fileContract = fileContractRepository.findFileByPriority(contractCode)
                .orElseThrow(() -> new RuntimeException("No contract file found for contract code: " + contractCode));

        Path path = Paths.get(fileContract.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("File not found or not readable: " + fileContract.getFilePath());
        }

        log.info("[End] Get contract file resource successfully for contract code: {}", contractCode);
        return resource;
    }
}
