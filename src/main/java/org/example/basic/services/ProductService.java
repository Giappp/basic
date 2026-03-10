package org.example.basic.services;

import lombok.RequiredArgsConstructor;
import org.example.basic.dto.PageResponse;
import org.example.basic.dto.ProductDTO;
import org.example.basic.entities.Category;
import org.example.basic.entities.Product;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.example.basic.mapper.ProductMapper;
import org.example.basic.repositories.CategoryRepository;
import org.example.basic.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductDTO> getAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper.INSTANCE::toDto)
                .toList();

    }

    public ProductDTO getById(Integer id) {
        Product product = getProductOrThrow(id);
        return ProductMapper.INSTANCE.toDto(product);
    }

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Category category = getCategoryOrThrow(dto.categoryId());

        Product product = ProductMapper.INSTANCE.toEntity(dto);
        product.setCategory(category);

        return ProductMapper.INSTANCE.toDto(productRepository.save(product));
    }

    @Transactional
    public ProductDTO update(Integer productId, ProductDTO dto) {
        Product product = getProductOrThrow(productId);

        if (product.getCategory() == null || !product.getCategory().getId().equals(dto.categoryId())) {
            Category category = categoryRepository.getReferenceById(dto.categoryId());
            product.setCategory(category);
        }

        product.setName(dto.name());
        product.setPrice(dto.price());

        return ProductMapper.INSTANCE.toDto(productRepository.save(product));
    }

    @Transactional
    public ProductDTO delete(Integer productId) {
        Product product = getProductOrThrow(productId);
        productRepository.delete(product);
        return ProductMapper.INSTANCE.toDto(product);
    }

    public PageResponse<ProductDTO> search(String name, Integer categoryId, Double minPrice, Double maxPrice, Pageable pageable) {
        Page<Product> productPage = productRepository.searchByHql(name, categoryId, minPrice, maxPrice, pageable);
        var products = productPage.getContent()
                .stream()
                .map(ProductMapper.INSTANCE::toDto)
                .toList();
        return PageResponse.<ProductDTO>builder()
                .data(products)
                .pageSize(productPage.getSize())
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .currentPage(productPage.getNumber())
                .build();
    }

    private Product getProductOrThrow(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    }

    private Category getCategoryOrThrow(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CATEGORY));
    }
}
