package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.service.ProductSyncService;
import com.XenoTest.Xeno.tenant.TenantContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync")
public class ProductSyncController {

    private final ProductSyncService service;

    public ProductSyncController(ProductSyncService service) {
        this.service = service;
    }

    @PostMapping("/products")
    public ResponseEntity<String> syncProducts() throws Exception {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            return ResponseEntity.badRequest().body("Missing tenant header 'X-Tenant-ID'");
        }

        String result = service.syncProducts(tenantId);

        return ResponseEntity.ok(result);
    }
}
