package org.example.basic.services;

import org.example.basic.dto.PageResponse;
import org.example.basic.dto.ProductCriteria;
import org.example.basic.dto.ProductDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    PageResponse<ProductDTO> getAll(Pageable pageable);

    ProductDTO getById(Integer id);

    ProductDTO create(ProductDTO dto);

    ProductDTO update(Integer productId, ProductDTO dto);

    ProductDTO delete(Integer productId);

    PageResponse<ProductDTO> search(ProductCriteria criteria, Pageable pageable);
}
