package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.entity.Product;
import com.XenoTest.Xeno.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProductsByTenant(Long tenantId) {
        return productRepository.findByTenantId(tenantId);
    }
}
