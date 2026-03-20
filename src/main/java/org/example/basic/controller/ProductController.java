package org.example.basic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.basic.dto.ApiResponse;
import org.example.basic.dto.PageResponse;
import org.example.basic.dto.ProductCriteria;
import org.example.basic.dto.ProductDTO;
import org.example.basic.services.ProductService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(
            summary = "Get List of Products include pagination and sorting",
            description = "Return a list of product"
    )
    @GetMapping("/")
    public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> getAll(@Parameter(description = "A Pageable Object")
                                                                        @ParameterObject @PageableDefault() Pageable pageable) {
        var productDtoList = productService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(productDtoList));
    }

    @Operation(
            summary = "Create Product"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product Information to create", required = true)
                                                                 @RequestBody @Valid ProductDTO dto) {
        var productDTO = productService.create(dto);
        return ResponseEntity.ok(ApiResponse.success(productDTO));
    }

    @Operation(
            summary = "Get Product By Id",
            description = "Returns detailed information of a product"
    )
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Integer productId) {
        var product = productService.getById(productId);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @Operation(
            summary = "Update entire product (Admin Only)"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable Integer productId, @RequestBody @Valid ProductDTO dto) {
        var product = productService.update(productId, dto);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @Operation(
            summary = "Delete product (Admin Only)"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> deleteProduct(@PathVariable Integer productId) {
        var result = productService.delete(productId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(
            summary = "Search Product",
            description = """
                    Search product in the system.
                    Options:
                    - Search by Name
                    - Filter by Category
                    - Price range (min,max)
                    """
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> searchProducts(ProductCriteria criteria,
                                                                                @ParameterObject @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(productService.search(criteria, pageable)));
    }
}
