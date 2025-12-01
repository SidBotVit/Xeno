package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.entity.Product;
import com.XenoTest.Xeno.repository.ProductRepository;
import com.XenoTest.Xeno.tenant.TenantContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/view/products")
public class ProductViewController {

    private final ProductRepository repo;

    public ProductViewController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Product> getProducts() {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null)
            throw new RuntimeException("Missing X-Tenant-ID");

        return repo.findByTenantId(tenantId);
    }
}
