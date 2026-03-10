package org.example.basic.mapper;

import org.example.basic.dto.ProductDTO;
import org.example.basic.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "category.id", target = "categoryId")
    ProductDTO toDto(Product product);

    Product toEntity(ProductDTO dto);
}
