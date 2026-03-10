package org.example.basic.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.basic.dto.ProductDTO;
import org.example.basic.services.ProductService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public ResponseEntity<?> get() {
        var productDtoList = productService.getAll();
        return ResponseEntity.ok(productDtoList);
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody @Valid ProductDTO dto) {
        var result = productService.create(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getById(@PathVariable Integer productId) {
        var product = productService.getById(productId);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> update(@PathVariable Integer productId, @RequestBody @Valid ProductDTO dto) {
        var product = productService.update(productId, dto);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> delete(@PathVariable Integer productId) {
        var result = productService.delete(productId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getProducts(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) Integer categoryId,
                                         @RequestParam(required = false) Double minPrice,
                                         @RequestParam(required = false) Double maxPrice,
                                         @ParameterObject @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(productService.search(name, categoryId, minPrice, maxPrice, pageable));
    }
}
