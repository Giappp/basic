package org.example.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDTO(
        @NotNull(message = "{validate.product.name-null}")
        @NotBlank(message = "{validate.product.name-null}")
        @Schema(description = "Product Name", example = "24-inch Monitor")
        String name,
        @NotNull(message = "{validate.product.price-null}")
        @Min(value = 0, message = "{validate.product.price-negative}")
        @Schema(description = "Price of the product in dollar", example = "50")
        Double price,
        @Schema(description = "Existed Category Id", example = "2")
        Integer categoryId,
        @Schema(description = "Category Name", example = "Book")
        String categoryName) {
}
