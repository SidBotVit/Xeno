package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.service.AnalyticsService;
import com.XenoTest.Xeno.tenant.TenantContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {

        Long tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(analyticsService.getSummary(tenantId));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Map<String, Object>>> getOrdersByDate(
            @RequestParam String start,
            @RequestParam String end
    ) {
        Long tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(analyticsService.getOrdersByDate(tenantId, start, end));
    }

    @GetMapping("/top-customers")
    public ResponseEntity<List<Map<String, Object>>> getTopCustomers() {

        Long tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(analyticsService.getTopCustomers(tenantId));
    }

    // -------------------- NEW --------------------
    @GetMapping("/revenue-by-country")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByCountry() {

        Long tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(analyticsService.getRevenueByCountry(tenantId));
    }

    @GetMapping("/customers-by-country")
    public ResponseEntity<List<Map<String, Object>>> getCustomersByCountry() {

        Long tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(analyticsService.getCustomersByCountry(tenantId));
    }

    @GetMapping("/new-vs-returning")
    public ResponseEntity<List<Map<String, Object>>> getNewVsReturning() {

        Long tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(analyticsService.getNewVsReturning(tenantId));
    }
}
