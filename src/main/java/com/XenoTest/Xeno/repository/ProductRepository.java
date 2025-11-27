package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTenantId(Long tenantId);
}
