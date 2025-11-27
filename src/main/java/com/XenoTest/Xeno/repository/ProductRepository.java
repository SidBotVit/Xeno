package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByShopifyProductIdAndTenantId(Long shopifyProductId, Long tenantId);
    List<Product> findByTenantId(Long tenantId);
}
