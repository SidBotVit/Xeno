package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.entity.Order;
import com.XenoTest.Xeno.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repo;

    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    public Order addOrder(Order order) {
        return repo.save(order);
    }

    public List<Order> getOrdersForTenant(Long tenantId) {
        return repo.findByTenantId(tenantId);
    }
}
