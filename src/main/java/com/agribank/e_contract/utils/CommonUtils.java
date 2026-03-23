package com.agribank.e_contract.utils;

import com.agribank.e_contract.dto.ContractCodeDTO;

import java.security.SecureRandom;

public class CommonUtils {
    public static String generateContractCode() {
        String generateRandomNumber = String.format("%06d", new SecureRandom().nextInt(1000000));
        return "CT" + generateRandomNumber;
    }
    public static String generateOTPCode() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }
    private static final String[] numbers = {
            "không","một","hai","ba","bốn","năm","sáu","bảy","tám","chín"
    };

    public static String numberToWords(long number) {
        if (number == 0) return "không";

        String[] units = {"", " nghìn", " triệu", " tỷ"};
        int unitIndex = 0;
        String result = "";

        while (number > 0) {
            int part = (int)(number % 1000);
            if (part != 0) {
                result = convertThreeDigits(part) + units[unitIndex] + " " + result;
            }
            number /= 1000;
            unitIndex++;
        }

        return result.replaceAll("\\s+", " ").trim() + " đồng";
    }

    private static String convertThreeDigits(int number) {
        int hundred = number / 100;
        int ten = (number % 100) / 10;
        int unit = number % 10;

        StringBuilder sb = new StringBuilder();

        if (hundred > 0) {
            sb.append(numbers[hundred]).append(" trăm ");
        }

        if (ten > 1) {
            sb.append(numbers[ten]).append(" mươi ");
            if (unit == 1) sb.append("mốt ");
            else if (unit == 5) sb.append("lăm ");
            else if (unit > 0) sb.append(numbers[unit]).append(" ");
        } else if (ten == 1) {
            sb.append("mười ");
            if (unit == 5) sb.append("lăm ");
            else if (unit > 0) sb.append(numbers[unit]).append(" ");
        } else if (unit > 0) {
            sb.append(numbers[unit]).append(" ");
        }

        return sb.toString();
    }
}
