package org.example.basic.handler;

import org.example.basic.dto.RequestResponse;
import org.example.basic.errors.Messages;
import org.example.basic.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException appException) {
        return ResponseEntity.badRequest().body(RequestResponse.builder()
                .messages(appException.getMessage())
                .code(appException.getCode())
                .status(false)
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (var e : exception.getFieldErrors()) {
            errors.put(e.getField(), e.getDefaultMessage());
        }
        return ResponseEntity.badRequest()
                .body(RequestResponse.builder()
                        .messages(errors)
                        .status(false)
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest()
                .body(RequestResponse.builder()
                        .messages(Messages.INVALID_BODY)
                        .status(false)
                        .build());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleGeneralException(Exception exception) {
//        return ResponseEntity.badRequest().body(RequestResponse.builder()
//                .messages(exception.get())
//                .status(false));
//    }
}
