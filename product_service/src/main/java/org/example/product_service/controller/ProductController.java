package org.example.product_service.controller;

import org.example.product_service.entity.Product;
import org.example.product_service.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/getProductByID/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = service.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @GetMapping("/getAllByName//{name}")
    public ResponseEntity<List<Product>> getAllProductsByName(@PathVariable String name) {
        List<Product> products = service.getAllProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getAllByCategory/{category}")
    public ResponseEntity<List<Product>> getAllProductsByCategory(@PathVariable String category) {
        List<Product> products = service.getAllProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getAllByPrice/{min}/{max}")
    public ResponseEntity<List<Product>> getAllProductsByPrice(@PathVariable BigDecimal min, @PathVariable BigDecimal max) {
        List<Product> products = service.getAllProductsByPrice(min, max);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getAllByCategoryAndPrice/{category}/{minPrice}/{maxPrice}")
    public ResponseEntity<List<Product>> findProductsByCategoryAndPriceRange(@PathVariable String category
            , @PathVariable BigDecimal minPrice, @PathVariable BigDecimal maxPrice) {
        List<Product> products = service.findProductsByCategoryAndPriceRange(category, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "name") String sortBy) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Page<Product> products = service.getAllProducts(PageRequest.of(page, size, Sort.by(sortBy)));
        return ResponseEntity.ok(products);

    }

    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product product1 = service.addProduct(product);
        return ResponseEntity.ok(product1);
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        return ResponseEntity.ok(service.updateProduct(product));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.ok("Продукт успешно удалён");
    }
}
