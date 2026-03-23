package org.example.basic.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import org.example.basic.dto.ApiResponse;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<String>> handleAppException(AppException appException) {
        return ResponseEntity.badRequest().body(ApiResponse.errorException(appException));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (var e : exception.getFieldErrors()) {
            errors.put(e.getField(), e.getDefaultMessage());
        }
        return ResponseEntity.badRequest()
                .body(ApiResponse.validationErrors(errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest()
                .body(ApiResponse
                        .errorMessage(resolveMessage(ErrorCode.INVALID_BODY), ErrorCode.INVALID_BODY.code));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<String>> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse
                        .errorMessage(resolveMessage(ErrorCode.INVALID_TOKEN), ErrorCode.INVALID_TOKEN.code));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<String>> handleSignatureException(SignatureException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.errorMessage(resolveMessage(ErrorCode.INVALID_TOKEN), ErrorCode.INVALID_TOKEN.code));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<String>> handleOtherJwtExceptions(JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.errorMessage(resolveMessage(ErrorCode.INVALID_TOKEN), ErrorCode.INVALID_TOKEN.code));
    }

    private String resolveMessage(ErrorCode errorCode) {
        return messageSource.getMessage(
                errorCode.key,
                null,
                LocaleContextHolder.getLocale()
        );
    }
}
