package com.agribank.e_contract.service.savingBook;

import com.agribank.e_contract.entity.SavingBook;
import com.agribank.e_contract.response.ResponseList;
import org.springframework.stereotype.Service;

@Service
public interface SavingBookService {
    ResponseList<SavingBook> getAllSavingBooks(String businessCode);
}
