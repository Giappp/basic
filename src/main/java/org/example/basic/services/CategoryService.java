package org.example.basic.services;

import org.example.basic.dto.CategoryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<CategoryDto> getListCategories(Pageable pageable);

    void create(CategoryDto categoryDto);

    void delete(Integer id);

    void update(Integer categoryId, CategoryDto categoryDto);
}
