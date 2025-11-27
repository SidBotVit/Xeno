package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.service.OrderSyncService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync")
public class OrderSyncController {

    private final OrderSyncService orderSyncService;

    public OrderSyncController(OrderSyncService orderSyncService) {
        this.orderSyncService = orderSyncService;
    }

    @GetMapping("/orders")
    public String syncOrders(
            @RequestParam Long tenantId,
            @RequestParam String shopDomain,
            @RequestParam String token
    ) {
        return orderSyncService.syncOrders(tenantId, shopDomain, token);
    }
}
