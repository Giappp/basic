package org.example.basic.services;

import lombok.RequiredArgsConstructor;
import org.example.basic.dto.CategoryDto;
import org.example.basic.entities.Category;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.example.basic.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getListCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public void create(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.name())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXIST);
        }
        categoryRepository.save(toEntity(categoryDto));
    }

    public void delete(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }

    private CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    private Category toEntity(CategoryDto dto) {
        Category category = new Category();
        category.setId(dto.id());
        category.setName(dto.name());
        return category;
    }
}
