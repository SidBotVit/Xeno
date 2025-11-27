package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.service.CustomerSyncService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync")
public class CustomerSyncController {

    private final CustomerSyncService customerSyncService;

    public CustomerSyncController(CustomerSyncService customerSyncService) {
        this.customerSyncService = customerSyncService;
    }

    @GetMapping("/customers")
    public String syncCustomers(
            @RequestParam Long tenantId,
            @RequestParam String shopDomain,
            @RequestParam String token
    ) {
        return customerSyncService.syncCustomers(tenantId, shopDomain, token);
    }
}
