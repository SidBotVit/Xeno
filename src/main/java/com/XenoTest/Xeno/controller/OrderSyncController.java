package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.service.OrderSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync")
public class OrderSyncController {

    private final OrderSyncService orderSyncService;

    public OrderSyncController(OrderSyncService orderSyncService) {
        this.orderSyncService = orderSyncService;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> syncOrders(@RequestHeader("X-Tenant-ID") Long tenantId) throws Exception {
        return ResponseEntity.ok(orderSyncService.syncOrders(tenantId));
    }

}
