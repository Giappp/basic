package org.example.basic.errors;

public enum ErrorCode {
    PRODUCT_NOT_EXISTS("Product deleted or not exists", "1000"),
    INVALID_CATEGORY("Category is invalid", "1001"),
    CATEGORY_ALREADY_EXIST("Category already exists", "1002"),
    CATEGORY_NOT_FOUND("Category deleted or not exists", "1003"),
    INVALID_CREDENTIALS("Wrong username or password", "2000"),
    EMAIL_ALREADY_INUSE("Email already used", "2001"),
    USERNAME_TAKEN("Username already taken", "2002"),
    PASSWORD_MISMATCH("Password and confirm password does not match", "2003"),
    TOKEN_EXPIRED("Token expired", "2004"),
    INVALID_TOKEN("Invalid Token or Already logout", "2005");
    public final String message;
    public final String code;

    ErrorCode(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
