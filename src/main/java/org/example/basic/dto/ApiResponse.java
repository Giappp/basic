package org.example.basic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String code;
    private String message;

    private T data;
    private Map<String, String> errors;

    private Long timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code("0")
                .message("OK")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .code(code)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static ApiResponse<Void> validationErrors(
            String code,
            String message,
            Map<String, String> errors
    ) {
        return ApiResponse.<Void>builder()
                .success(false)
                .message(message)
                .code(code)
                .errors(errors)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}