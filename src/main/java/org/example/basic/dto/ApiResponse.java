package org.example.basic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private T data;
    private T messages;
    private String code;
    private Boolean success;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .success(true)
                .build();
    }

    public static ApiResponse<String> errorException(AppException ex) {
        return ApiResponse.<String>builder()
                .messages(ex.getMessage())
                .code(ex.getCode())
                .success(false)
                .build();
    }

    public static ApiResponse<String> errorMessage(ErrorCode error) {
        return ApiResponse.<String>builder()
                .messages(error.message)
                .code(error.code)
                .success(false)
                .build();
    }

    public static <T> ApiResponse<T> validationErrors(T errors) {
        return ApiResponse.<T>builder()
                .messages(errors)
                .success(false)
                .build();
    }
}
