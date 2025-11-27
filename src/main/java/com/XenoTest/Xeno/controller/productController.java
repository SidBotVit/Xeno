package com.XenoTest.Xeno.controller;


import com.XenoTest.Xeno.entity.Product;
import com.XenoTest.Xeno.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class productController {

    private final ProductService productService;

    public productController(ProductService productService) {
        this.productService = productService;
    }

    // Create product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.addProduct(product);
        return ResponseEntity.ok(created);
    }

    // Get all products for a tenant
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Product>> listByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(productService.getProductsByTenant(tenantId));
    }

    // Get product for a tenant
    @GetMapping("/{id}/tenant/{tenantId}")
    public ResponseEntity<Product> getProduct(
            @PathVariable Long id,
            @PathVariable Long tenantId
    ) {
        return productService.getProduct(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
