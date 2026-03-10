package org.example.basic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestResponse<T> {
    private T data;
    private T messages;
    private String code;
    private Boolean status;
}
