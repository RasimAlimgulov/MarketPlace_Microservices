package org.example.product_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.product_service.entity.Product;
import org.example.product_service.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> getAllProductsByName(String name) {
        return repository.getProductsByName(name);
    }

    @Override
    public List<Product> getAllProductsByCategory(String category) {
        return repository.getProductsByCategory(category);
    }

    @Override
    public List<Product> getAllProductsByPrice(BigDecimal min, BigDecimal max) {
        return repository.getProductsByPriceBetween(min,max);
    }
    @Override
    public List<Product> findProductsByCategoryAndPriceRange(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return repository.getProductsByCategoryAndPriceBetween(category,minPrice,maxPrice);
    }
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Product addProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public String deleteProduct(Long id) {
        if (getProductById(id)==null){
            return "User not found";
        }
        repository.deleteById(id);
        return "Delete was successful" ;
    }

    @Override
    public Product updateProduct(Product product) {
        if (repository.existsById(product.getId())){
            return repository.save(product);
        }else
        throw new EntityNotFoundException("Product with id " + product.getId() + " not found") ;
    }

    @Override
    public Product getProductById(Long id) {
        Product product=repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Product with id " + id + " not found"));
        return product;
    }

}
