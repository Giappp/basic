package org.example.basic.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.basic.dto.ProductDTO;
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
    @Operation(
            summary = "Get List of Categories include pagination and sorting",
            description = "Return a list of product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns categories in page"),
            @ApiResponse(responseCode = "500", description = "Server Error")
    })
    @GetMapping("/")
    public ResponseEntity<?> get(@Parameter(description = "A Pageable Object")
                                 @ParameterObject @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok().build();
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
    public ResponseEntity<?> create(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category Information to create", required = true)
                                    @RequestBody @Valid ProductDTO dto) {
        return ResponseEntity.ok().build();
    }
}
