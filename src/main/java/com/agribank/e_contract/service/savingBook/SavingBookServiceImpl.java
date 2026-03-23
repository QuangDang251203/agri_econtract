package com.agribank.e_contract.service.savingBook;

import com.agribank.e_contract.entity.SavingBook;
import com.agribank.e_contract.repository.SavingBookRepository;
import com.agribank.e_contract.response.ResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingBookServiceImpl implements SavingBookService {
    private final SavingBookRepository savingBookRepo;
    public ResponseList<SavingBook> getAllSavingBooks(String businessCode) {
        List<SavingBook> savingBooks = savingBookRepo.findByBusinessCode(businessCode);
        return ResponseList.success(savingBooks);
    }
}
