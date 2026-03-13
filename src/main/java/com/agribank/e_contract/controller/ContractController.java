package com.agribank.e_contract.controller;

import com.agribank.e_contract.dto.ContractDTO;
import com.agribank.e_contract.dto.ContractRequest;
import com.agribank.e_contract.dto.UpdateContractDTO;
import com.agribank.e_contract.response.CommonResponse;
import com.agribank.e_contract.service.contract.ContractService;
import com.deepoove.poi.XWPFTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true" )
public class ContractController {
    public final ContractService contractService;

    @PostMapping("/createContract")
    public CommonResponse createContract(@Valid @RequestBody ContractDTO dto) {
        return contractService.createContract(dto);
    }

    @PostMapping("/signContract")
    public CommonResponse signContract(@RequestParam String OtpCode,
                                       @RequestParam String contract) {
        return contractService.signContract(OtpCode, contract);
    }

    @PutMapping("/deleteContract/{contractCode}")
    public CommonResponse deleteContract(@PathVariable String contractCode) {
        return contractService.deleteContract(contractCode);
    }

//    @PostMapping("/generate-and-download")
//    public ResponseEntity<byte[]> generateAndDownloadContract(@RequestBody ContractRequest request) throws IOException {
//        Map<String, Object> data = new HashMap<>();
//        data.put("ten_bien", "Số: 01/2026/E-CON");
//        data.put("ten_khach_hang", request.getCustomerName());
//        data.put("dia_chi", request.getAddress());
//
//        ClassPathResource resource = new ClassPathResource("templates/ex_template.docx");
//
//        String savePath = "C:/Users/Hi/OneDrive - utt.vn/CÔNG VIỆC/Template_demo/";
//        File dir = new File(savePath);
//        if (!dir.exists()) dir.mkdirs();
//
//        String fileName = "hop_dong_" + System.currentTimeMillis() + ".docx";
//
//        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
//             FileOutputStream fos = new FileOutputStream(new File(savePath + fileName))) {
//
//            XWPFTemplate template = XWPFTemplate.compile(resource.getInputStream()).render(data);
//            template.write(out);
//            template.write(fos);
//            template.close();
//            byte[] bytes = out.toByteArray();
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
//                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
//                    .body(bytes);
//        }
//    }
}


