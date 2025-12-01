package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.entity.Order;
import com.XenoTest.Xeno.repository.OrderRepository;
import com.XenoTest.Xeno.tenant.TenantContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderViewController {

    private final OrderRepository repo;

    public OrderViewController(OrderRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Order> getOrders() {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null)
            throw new RuntimeException("Missing X-Tenant-ID");

        return repo.findByTenantId(tenantId);
    }
}
