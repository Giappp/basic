package org.example.basic.exception;

import lombok.Getter;
import org.example.basic.errors.ErrorCode;

@Getter
public class AppException extends RuntimeException {
    private final String message;
    private final String code;

    public AppException(ErrorCode errorCode) {
        message = errorCode.message;
        code = errorCode.code;
    }
}
