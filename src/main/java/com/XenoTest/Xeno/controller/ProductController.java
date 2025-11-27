package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.entity.Product;
import com.XenoTest.Xeno.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping("/{tenantId}")
    public List<Product> getProducts(@PathVariable Long tenantId) {
        return productService.getProductsByTenant(tenantId);
    }
}
