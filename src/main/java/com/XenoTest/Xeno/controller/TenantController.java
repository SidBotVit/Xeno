package com.XenoTest.Xeno.controller;


import com.XenoTest.Xeno.entity.Tenant;
import com.XenoTest.Xeno.service.TenantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;

    }
    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant){
        Tenant created = tenantService.createTenant(tenant);
        return ResponseEntity.ok(created);
    }
    @GetMapping
    public ResponseEntity<List<Tenant>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getTenant(@PathVariable Long id) {
        return tenantService.getTenantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
