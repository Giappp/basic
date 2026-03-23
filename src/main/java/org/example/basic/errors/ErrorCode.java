package org.example.basic.errors;

public enum ErrorCode {
    INVALID_BODY("payload.invalid", "1"),
    PRODUCT_NOT_EXISTS("product.not-found", "1000"),
    CATEGORY_NOT_FOUND("category.not-found", "1001"),
    CATEGORY_ALREADY_EXIST("category.duplicate", "1002"),

    INVALID_CREDENTIALS("credentials.invalid", "2000"),
    EMAIL_TAKEN("email.taken", "2001"),
    USERNAME_TAKEN("username.taken", "2002"),
    PASSWORD_MISMATCH("password.mismatch", "2003"),
    TOKEN_EXPIRED("token.expired", "2004"),
    INVALID_TOKEN("token.invalidate", "2005"),
    USER_NOT_FOUND("user.not-found", "2006");

    public final String key;
    public final String code;

    ErrorCode(String key, String code) {
        this.key = key;
        this.code = code;
    }
}
