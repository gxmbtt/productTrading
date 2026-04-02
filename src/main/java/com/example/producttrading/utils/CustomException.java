package com.example.producttrading.utils;

/**
 * @author guoxin
 * @date 2026年04月02日 17:00
 */
public class CustomException extends RuntimeException{
    private int code;
    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(String message) {
        super(message);
        this.code = 500;
    }
    public int getCode() {
        return code;
    }
}
