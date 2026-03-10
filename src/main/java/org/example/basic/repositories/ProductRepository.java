package org.example.basic.repositories;

import org.example.basic.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("""
             SELECT p FROM Product p
             WHERE (CAST(:name AS STRING ) IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:name AS STRING), '%')))
               AND (CAST(:categoryId AS INTEGER ) IS NULL OR p.category.id = :categoryId)
               AND (CAST(:minPrice AS DOUBLE ) IS NULL OR p.price >= :minPrice)
               AND (CAST(:maxPrice AS DOUBLE) IS NULL OR p.price <= :maxPrice)
            """)
    Page<Product> searchByHql(@Param("name") String name, @Param("categoryId") Integer categoryId,
                              @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);
}
