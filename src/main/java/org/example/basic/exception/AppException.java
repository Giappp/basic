package org.example.basic.exception;

import lombok.Getter;
import org.example.basic.errors.ErrorCode;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.key);
        this.errorCode = errorCode;
    }
}
