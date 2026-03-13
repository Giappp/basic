package org.example.basic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.basic.dto.CategoryDto;
import org.example.basic.services.CategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Get List of Categories include pagination and sorting",
            description = "Return a list of product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns categories in page"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @GetMapping("/")
    public ResponseEntity<?> getCategories(@Parameter(description = "A Pageable Object")
                                           @ParameterObject @PageableDefault() Pageable pageable) {
        var result = categoryService.getListCategories(pageable);
        return ResponseEntity.ok(org.example.basic.dto.ApiResponse.builder()
                .data(result)
                .success(true)
                .build());
    }

    @Operation(
            summary = "Create Category"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Create Success"),
            @ApiResponse(responseCode = "405", description = "Required Permission")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> createCategory(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category Information to create", required = true)
                                            @RequestBody @Valid CategoryDto dto) {
        categoryService.create(dto);
        return ResponseEntity.ok(org.example.basic.dto.ApiResponse.builder()
                .messages("Add new Category Success")
                .success(true)
                .build());
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCategory(Integer categoryId, CategoryDto categoryDto) {
        categoryService.update(categoryId, categoryDto);
        return ResponseEntity.ok(org.example.basic.dto.ApiResponse.builder()
                .messages("Update Category Success")
                .success(true)
                .build());
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(Integer categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.ok(org.example.basic.dto.ApiResponse.builder()
                .messages("Delete Category Success")
                .success(true)
                .build());
    }
}
