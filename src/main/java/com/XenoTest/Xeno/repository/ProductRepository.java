package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTenantId(Long tenantId);

    Page<Product> findByTenantIdAndTitleContainingIgnoreCase(
            Long tenantId,
            String title,
            Pageable pageable
    );

    Optional<Product> findByShopifyProductIdAndTenantId(Long shopifyProductId, Long tenantId);
}
