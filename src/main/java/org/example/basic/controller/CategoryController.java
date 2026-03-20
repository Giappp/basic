package org.example.basic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.basic.dto.ApiResponse;
import org.example.basic.dto.CategoryDto;
import org.example.basic.services.CategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Get List of Categories include pagination and sorting",
            description = "Return a list of product"
    )
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategories(@Parameter(description = "A Pageable Object")
                                                                        @ParameterObject @PageableDefault() Pageable pageable) {
        var result = categoryService.getListCategories(pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(
            summary = "Create Category"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ApiResponse<String>> createCategory(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category Information to create", required = true)
                                                              @RequestBody @Valid CategoryDto dto) {
        categoryService.create(dto);
        return ResponseEntity.ok(ApiResponse.success(""));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateCategory(@PathVariable Integer id, CategoryDto categoryDto) {
        categoryService.update(id, categoryDto);
        return ResponseEntity.ok(org.example.basic.dto.ApiResponse.success(""));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
        return ResponseEntity.ok(org.example.basic.dto.ApiResponse.success(""));
    }
}
