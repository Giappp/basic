package org.example.basic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDTO(
        @NotNull(message = "Name must not be null")
        @NotBlank(message = "Name must not be blank")
        String name,
        @NotNull(message = "Price must not be null")
        @Min(value = 0, message = "Price must greater than 0")
        Double price,
        Integer categoryId,
        String categoryName) {
}
