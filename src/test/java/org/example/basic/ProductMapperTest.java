package org.example.basic;

import org.example.basic.dto.ProductDTO;
import org.example.basic.entities.Product;
import org.example.basic.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ProductMapperTest {
    @Test
    public void shouldMapProductDto() {
        Product product = new Product(1, "Test Product 123", 12.0);
        ProductDTO dto = ProductMapper.INSTANCE.toDto(product);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getName()).isEqualTo("Test Product 123");
        assertThat(dto.getPrice()).isEqualTo(12.0);
    }

}
