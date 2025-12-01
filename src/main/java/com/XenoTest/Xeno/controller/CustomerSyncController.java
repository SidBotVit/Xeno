package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.service.CustomerSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync")
public class CustomerSyncController {

    private final CustomerSyncService customerSyncService;

    public CustomerSyncController(CustomerSyncService customerSyncService) {
        this.customerSyncService = customerSyncService;
    }

    @PostMapping("/customers")
    public ResponseEntity<String> syncCustomers(@RequestHeader("X-Tenant-ID") Long tenantId) throws Exception {
        return ResponseEntity.ok(customerSyncService.syncCustomers(tenantId));
    }
}
