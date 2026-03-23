package org.example.basic.errors;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_BODY("payload.invalid", "1", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTS("product.not-found", "1000", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("category.not-found", "1001", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXIST("category.duplicate", "1002", HttpStatus.CONFLICT),

    INVALID_CREDENTIALS("credentials.invalid", "2000", HttpStatus.UNAUTHORIZED),
    EMAIL_TAKEN("email.taken", "2001", HttpStatus.CONFLICT),
    USERNAME_TAKEN("username.taken", "2002", HttpStatus.CONFLICT),
    PASSWORD_MISMATCH("password.mismatch", "2003", HttpStatus.BAD_REQUEST),

    TOKEN_EXPIRED("token.expired", "2004", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("token.invalid", "2005", HttpStatus.UNAUTHORIZED),

    USER_NOT_FOUND("user.not-found", "2006", HttpStatus.NOT_FOUND),

    INTERNAL_SERVER_ERROR("internal.server-error", "9999", HttpStatus.INTERNAL_SERVER_ERROR);

    public final String key;
    public final String code;
    public final HttpStatus status;

    ErrorCode(String key, String code, HttpStatus status) {
        this.key = key;
        this.code = code;
        this.status = status;
    }
}
