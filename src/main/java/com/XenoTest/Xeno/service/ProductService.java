package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.entity.Product;
import com.XenoTest.Xeno.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
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

    // ⭐ Pagination + Search
    public Page<Product> getProducts(Long tenantId, int page, int size, String search) {

        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findByTenantIdAndTitleContainingIgnoreCase(
                tenantId,
                search == null ? "" : search,
                pageable
        );
    }
}
