package com.agribank.e_contract.controller;

import com.agribank.e_contract.entity.SavingBook;
import com.agribank.e_contract.response.ResponseList;
import com.agribank.e_contract.service.savingBook.SavingBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saving-books")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true" )
public class SavingBookController {
    private final SavingBookService savingBookService;
    @PostMapping("getAllByBusinessCode/{businessCode}")
    public ResponseList<SavingBook> getAllSavingBooks(@PathVariable String businessCode) {
        return savingBookService.getAllSavingBooks(businessCode);
    }
}
