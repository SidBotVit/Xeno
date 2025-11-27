package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByTenantId(Long tenantId);
}