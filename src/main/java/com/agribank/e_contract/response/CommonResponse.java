package com.agribank.e_contract.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    private String code;
    private String message;

    public static CommonResponse success() {
        return new CommonResponse("00", "success");
    }
}
