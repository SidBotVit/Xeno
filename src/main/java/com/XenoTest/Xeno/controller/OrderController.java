package com.XenoTest.Xeno.controller;


import com.XenoTest.Xeno.entity.Order;
import com.XenoTest.Xeno.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.addOrder(order));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Order>> getOrdersForTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(orderService.getOrdersForTenant(tenantId));
    }
}
