package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.service.OrderSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync")
public class OrderSyncController {

    private final OrderSyncService service;

    public OrderSyncController(OrderSyncService service) {
        this.service = service;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> syncOrders() throws Exception {
        return ResponseEntity.ok(service.syncOrders());
    }
}
