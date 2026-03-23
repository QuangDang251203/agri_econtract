package com.agribank.e_contract.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject<T> {
    private String code;
    private String message;
    private T data;

    public static <T> ResponseObject<T> success(T data) {
        return new ResponseObject<>("00", "Success", data);
    }
}

