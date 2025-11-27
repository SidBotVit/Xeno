package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.entity.Order;
import com.XenoTest.Xeno.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repo;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/db")
    public List<Order> getOrders(@RequestParam Long tenantId) {
        return repo.findByTenantId(tenantId);
    }
}
