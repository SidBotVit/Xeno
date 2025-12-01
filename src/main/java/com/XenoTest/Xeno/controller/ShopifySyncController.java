package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.service.OrderSyncService;
import com.XenoTest.Xeno.service.CustomerSyncService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopify/sync")
public class ShopifySyncController {

    private final OrderSyncService orderSyncService;
    private final CustomerSyncService customerSyncService;

    public ShopifySyncController(OrderSyncService orderSyncService,
                                 CustomerSyncService customerSyncService) {
        this.orderSyncService = orderSyncService;
        this.customerSyncService = customerSyncService;
    }

    // 👉 Sync Orders
    @PostMapping("/orders")
    public String syncOrders(@RequestHeader("X-Tenant-ID") Long tenantId) throws Exception {
        return orderSyncService.syncOrders(tenantId);
    }

    // 👉 Sync Customers
    @PostMapping("/customers")
    public String syncCustomers(@RequestHeader("X-Tenant-ID") Long tenantId) throws Exception {
        return customerSyncService.syncCustomers(tenantId);
    }
}
