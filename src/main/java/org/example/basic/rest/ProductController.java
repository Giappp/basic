package org.example.basic.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns products in page"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @GetMapping("/")
    public ResponseEntity<?> get(@Parameter(description = "A Pageable Object")
                                 @ParameterObject @PageableDefault() Pageable pageable) {
        var productDtoList = productService.getAll(pageable);
        return ResponseEntity.ok(productDtoList);
    }

    @Operation(
            summary = "Create Product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Create product success"),
            @ApiResponse(responseCode = "405", description = "Required Permission")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> create(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product Information to create", required = true)
                                    @RequestBody @Valid ProductDTO dto) {
        var result = productService.create(dto);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Get Product By Id",
            description = "Returns detailed information of a product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<?> getById(@PathVariable Integer productId) {
        var product = productService.getById(productId);
        return ResponseEntity.ok(product);
    }

    @Operation(
            summary = "Update entire product (Admin Only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product Update Success"),
            @ApiResponse(responseCode = "405", description = "Required Permission")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<?> update(@PathVariable Integer productId, @RequestBody @Valid ProductDTO dto) {
        var product = productService.update(productId, dto);
        return ResponseEntity.ok(product);
    }

    @Operation(
            summary = "Delete product (Admin Only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product Deletion Success"),
            @ApiResponse(responseCode = "405", description = "Required Permission")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> delete(@PathVariable Integer productId) {
        var result = productService.delete(productId);
        return ResponseEntity.ok(result);
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns result page"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search")
    public ResponseEntity<?> getProducts(ProductCriteria criteria,
                                         @ParameterObject @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(productService.search(criteria, pageable));
    }
}
