package org.example.basic.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.basic.dto.CategoryDto;
import org.example.basic.entities.Category;
import org.example.basic.errors.ErrorCode;
import org.example.basic.exception.AppException;
import org.example.basic.repositories.CategoryRepository;
import org.example.basic.services.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getListCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public void create(CategoryDto categoryDto) {
        validate(categoryDto);
        categoryRepository.save(toEntity(categoryDto));
    }

    public void delete(Integer id) {
        Category category = getCategory(id);
        categoryRepository.delete(category);
    }

    public void update(Integer categoryId, CategoryDto categoryDto) {
        validate(categoryDto);

        Category category = getCategory(categoryId);
        category.setName(categoryDto.name());

        categoryRepository.save(category);
    }

    private void validate(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.name())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXIST);
        }
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

    private Category getCategory(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
