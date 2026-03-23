package org.example.basic.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.basic.dto.PageResponse;
import org.example.basic.dto.ProductCriteria;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public PageResponse<ProductDTO> getAll(Pageable pageable) {
        return getProductDTOPageResponse(productRepository.getProductsWithCategory(pageable));
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
        var saved = productRepository.save(product);
        log.info("Saved product {} successfully", saved.getId());
        return ProductMapper.INSTANCE.toDto(saved);
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
        Product savedProduct = productRepository.save(product);
        log.info("Update product {} successfully", productId);
        return ProductMapper.INSTANCE.toDto(savedProduct);
    }

    @Transactional
    public ProductDTO delete(Integer productId) {
        Product product = getProductOrThrow(productId);
        productRepository.delete(product);
        log.info("Delete product {} successfully", productId);
        return ProductMapper.INSTANCE.toDto(product);
    }

    public PageResponse<ProductDTO> search(ProductCriteria criteria, Pageable pageable) {
        Page<Product> productPage = productRepository.searchByHql(criteria.name(), criteria.categoryId(), criteria.minPrice(), criteria.maxPrice(), pageable);
        log.info("Retrieve product with criteria {}", criteria);
        return getProductDTOPageResponse(productPage);
    }

    private PageResponse<ProductDTO> getProductDTOPageResponse(Page<Product> productPage) {
        var products = productPage.getContent()
                .stream()
                .map(ProductMapper.INSTANCE::toDto)
                .toList();
        log.info("Retrieve page {} out of {} pages", productPage.getNumber(), productPage.getTotalPages());
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
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
