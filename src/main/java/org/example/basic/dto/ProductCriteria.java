package org.example.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductCriteria(
        @Schema(description = "Product Name filter option")
        String name,
        @Schema(description = "Category filter option by Id")
        Integer categoryId,
        @Schema(description = "Minimum price filter option")
        Double minPrice,
        @Schema(description = "Maximum price filter option")
        Double maxPrice
) {
}
