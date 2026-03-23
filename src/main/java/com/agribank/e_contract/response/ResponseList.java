package com.agribank.e_contract.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseList<T> {
    private String code;
    private String message;
    private List<T> data;

    public static <T> ResponseList<T> success(List<T> data) {
        return new ResponseList<>("00", "Success", data);
    }
}

