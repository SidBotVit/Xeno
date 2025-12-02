package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.repository.OrderRepository;
import com.XenoTest.Xeno.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AnalyticsService {

    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;

    public AnalyticsService(OrderRepository orderRepo, CustomerRepository customerRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
    }

    // -------------------- SUMMARY --------------------
    public Map<String, Object> getSummary(Long tenantId) {

        Map<String, Object> result = new HashMap<>();

        Long totalCustomers = customerRepo.countByTenantId(tenantId);
        Long totalOrders = orderRepo.countByTenantId(tenantId);
        Double totalRevenue = orderRepo.sumTotalPriceByTenant(tenantId);

        double aov = (totalOrders != null && totalOrders > 0)
                ? (totalRevenue == null ? 0 : totalRevenue) / totalOrders
                : 0;

        result.put("totalCustomers", totalCustomers);
        result.put("totalOrders", totalOrders);
        result.put("totalRevenue", totalRevenue == null ? 0.0 : totalRevenue);
        result.put("averageOrderValue", aov);

        return result;
    }

    // -------------------- ORDERS BY DATE --------------------
    public List<Map<String, Object>> getOrdersByDate(Long tenantId, String start, String end) {

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        return orderRepo.ordersByDate(tenantId, startDate, endDate);
    }

    // -------------------- TOP CUSTOMERS --------------------
    public List<Map<String, Object>> getTopCustomers(Long tenantId) {
        return orderRepo.topCustomers(tenantId);
    }

    // -------------------- NEW: Revenue By Country --------------------
    public List<Map<String, Object>> getRevenueByCountry(Long tenantId) {
        return orderRepo.revenueByCountry(tenantId);
    }

    // -------------------- NEW: Customers By Country --------------------
    public List<Map<String, Object>> getCustomersByCountry(Long tenantId) {
        return customerRepo.customersByCountry(tenantId);
    }

    // -------------------- NEW: Cohort / New vs Returning --------------------
    public List<Map<String, Object>> getNewVsReturning(Long tenantId) {
        return orderRepo.newVsReturning(tenantId);
    }
}
