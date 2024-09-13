package org.example.product_service.service;

import org.example.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProductsByName(String name);
    List<Product> getAllProductsByCategory(String category);
    List<Product> getAllProductsByPrice(BigDecimal min, BigDecimal max);
    List<Product> findProductsByCategoryAndPriceRange(String category,BigDecimal minPrice,BigDecimal maxPrice);
    Page<Product> getAllProducts(Pageable pageable);
    Product addProduct(Product product);
    String deleteProduct(Long id);
    Product updateProduct(Product product);
    Product getProductById(Long id);

}
