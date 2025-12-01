package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.repository.OrderRepository;
import com.XenoTest.Xeno.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AnalyticsService {

    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;

    public AnalyticsService(OrderRepository orderRepo, CustomerRepository customerRepo) {
        this.orderRepo = orderRepo;   // ✅ FIX: injected properly
        this.customerRepo = customerRepo;
    }

    // SUMMARY
    public Map<String, Object> getSummary(Long tenantId) {

        Map<String, Object> result = new HashMap<>();

        Long totalCustomers = customerRepo.countByTenantId(tenantId);
        Long totalOrders = orderRepo.countByTenantId(tenantId);
        Double totalRevenue = orderRepo.sumTotalPriceByTenant(tenantId);

        result.put("totalCustomers", totalCustomers);
        result.put("totalOrders", totalOrders);
        result.put("totalRevenue", totalRevenue == null ? 0.0 : totalRevenue);

        return result;
    }

    // ORDERS BY DATE
    public List<Map<String, Object>> getOrdersByDate(Long tenantId, String start, String end) {

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        return orderRepo.ordersByDate(tenantId, startDate, endDate);
    }

    // TOP CUSTOMERS
    public List<Map<String, Object>> getTopCustomers(Long tenantId) {
        return orderRepo.topCustomers(tenantId);
    }
}
