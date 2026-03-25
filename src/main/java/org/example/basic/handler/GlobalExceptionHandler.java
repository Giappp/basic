package org.example.basic.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.example.basic.dto.ApiResponse;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.jspecify.annotations.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<String>> handleAppException(AppException ex) {
        log.warn("Business Exception: ", ex);
        String message = resolveMessage(ex.getErrorCode());

        return ResponseEntity
                .status(ex.getErrorCode().status)
                .body(ApiResponse.error(message, ex.getErrorCode().code));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        log.warn("Validation Exception: ", ex);
        Map<String, String> errors = getFieldErrors(ex, locale);

        return ResponseEntity.badRequest().body(
                ApiResponse.validationErrors(
                        ErrorCode.INVALID_BODY.code,
                        messageSource.getMessage(ErrorCode.INVALID_BODY.key, null, locale),
                        errors
                )
        );
    }

    private @NonNull Map<String, String> getFieldErrors(MethodArgumentNotValidException ex, Locale locale) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String message = resolveFieldError(fieldError, locale);
            errors.putIfAbsent(fieldError.getField(), message);
        }
        return errors;
    }

    private @NonNull String resolveFieldError(FieldError fieldError, Locale locale) {
        return messageSource.getMessage(
                fieldError,
                locale
        );
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<String>> handleJwtException(JwtException ex) {
        log.warn("JWT Exception: ", ex);
        ErrorCode errorCode = mapJwtException(ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(
                        resolveMessage(errorCode),
                        errorCode.code
                ));
    }

    private ErrorCode mapJwtException(JwtException ex) {
        if (ex instanceof ExpiredJwtException) {
            return ErrorCode.TOKEN_EXPIRED;
        }
        if (ex instanceof SignatureException) {
            return ErrorCode.INVALID_TOKEN;
        }
        return ErrorCode.INVALID_TOKEN;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleBadRequest(HttpMessageNotReadableException ex) {

        log.warn(ex.getMostSpecificCause().getMessage());

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(
                        resolveMessage(ErrorCode.INVALID_BODY),
                        ErrorCode.INVALID_BODY.code
                ));
    }

    private String resolveMessage(ErrorCode errorCode) {
        return messageSource.getMessage(
                errorCode.key,
                null,
                LocaleContextHolder.getLocale()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleUnknown(Exception ex) {
        log.error("Internal Server error: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "Internal server error",
                        "9999"
                ));
    }
}
