package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.entity.Product;
import com.XenoTest.Xeno.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get products without pagination (raw list)
    @GetMapping("/db")
    public ResponseEntity<?> getProductsFromDb(
            @RequestHeader("X-Tenant-ID") Long tenantId) {

        return ResponseEntity.ok(productService.getProductsByTenant(tenantId));
    }

    // ⭐ Pagination + Search
    @GetMapping
    public ResponseEntity<?> listProducts(
            @RequestHeader("X-Tenant-ID") Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {

        return ResponseEntity.ok(
                productService.getProducts(tenantId, page, size, search)
        );
    }
}
