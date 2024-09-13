package org.example.product_service.repository;

import org.example.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> getProductsByName(String name);
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByPriceBetween(BigDecimal min, BigDecimal max);
    List<Product> getProductsByCategoryAndPriceBetween(String category,BigDecimal min,BigDecimal max);

    Page<Product> findAll(Pageable pageable);
}
