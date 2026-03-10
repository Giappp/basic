package org.example.basic.errors;

public enum ErrorCode {
    PRODUCT_NOT_EXISTS("Product deleted or not exists", "1000"),
    INVALID_CATEGORY("Category is invalid", "1001");
    public final String message;
    public final String code;

    ErrorCode(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
