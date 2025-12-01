package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByShopifyOrderIdAndTenantId(Long shopifyOrderId, Long tenantId);
    List<Order> findByTenantId(Long tenantId);
}
