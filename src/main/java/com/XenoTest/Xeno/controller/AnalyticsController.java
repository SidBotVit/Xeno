package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.service.AnalyticsService;
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

    // 👉 Used by /analytics/summary
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary(
            @RequestHeader("X-Tenant-ID") Long tenantId
    ) {
        return ResponseEntity.ok(analyticsService.getSummary(tenantId));
    }

    // 👉 Used by /analytics/orders?start=YYYY-MM-DD&end=YYYY-MM-DD
    @GetMapping("/orders")
    public ResponseEntity<List<Map<String, Object>>> getOrdersByDate(
            @RequestHeader("X-Tenant-ID") Long tenantId,
            @RequestParam String start,
            @RequestParam String end
    ) {
        return ResponseEntity.ok(analyticsService.getOrdersByDate(tenantId, start, end));
    }

    // 👉 Used by /analytics/top-customers
    @GetMapping("/top-customers")
    public ResponseEntity<List<Map<String, Object>>> getTopCustomers(
            @RequestHeader("X-Tenant-ID") Long tenantId
    ) {
        return ResponseEntity.ok(analyticsService.getTopCustomers(tenantId));
    }
}
