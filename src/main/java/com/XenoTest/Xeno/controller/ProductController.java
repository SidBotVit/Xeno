package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.entity.Product;
import com.XenoTest.Xeno.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/db")
    public List<Product> getProductsFromDb(@RequestParam Long tenantId) {
        return productRepository.findByTenantId(tenantId);
    }
}
