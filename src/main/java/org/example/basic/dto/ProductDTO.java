package org.example.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDTO(
        @NotNull(message = "Name must not be null")
        @NotBlank(message = "Name must not be blank")
        @Schema(description = "Product Name", example = "24-inch Monitor")
        String name,
        @NotNull(message = "Price must not be null")
        @Min(value = 0, message = "Price must greater than 0")
        @Schema(description = "Price of the product in dollar", example = "50")
        Double price,
        @Schema(description = "Existed Category Id", example = "2")
        Integer categoryId,
        @Schema(description = "Category Name", example = "Book")
        String categoryName) {
}
