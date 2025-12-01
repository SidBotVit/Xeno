package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.service.CustomerSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync")
public class CustomerSyncController {

    private final CustomerSyncService service;

    public CustomerSyncController(CustomerSyncService service) {
        this.service = service;
    }

    @PostMapping("/customers")
    public ResponseEntity<String> syncCustomers() throws Exception {
        return ResponseEntity.ok(service.syncCustomers());
    }
}
