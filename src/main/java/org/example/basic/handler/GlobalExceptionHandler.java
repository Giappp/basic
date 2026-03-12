package org.example.basic.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import org.example.basic.dto.ApiResponse;
import org.example.basic.errors.Messages;
import org.example.basic.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException appException) {
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .messages(appException.getMessage())
                .code(appException.getCode())
                .success(false)
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (var e : exception.getFieldErrors()) {
            errors.put(e.getField(), e.getDefaultMessage());
        }
        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                        .messages(errors)
                        .success(false)
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                        .messages(Messages.INVALID_BODY)
                        .success(false)
                        .build());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Unauthorized");
        error.put("message", "JWT token has expired. Please refresh.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.<Map<String, String>>builder()
                        .messages(error)
                        .success(false)
                        .build());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleSignatureException(SignatureException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Unauthorized");
        error.put("message", "Invalid JWT signature.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.<Map<String, String>>builder()
                        .messages(error)
                        .success(false)
                        .build());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleOtherJwtExceptions(JwtException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Unauthorized");
        error.put("message", "Invalid JWT token.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.<Map<String, String>>builder()
                        .messages(error)
                        .success(false)
                        .build());
    }
}
