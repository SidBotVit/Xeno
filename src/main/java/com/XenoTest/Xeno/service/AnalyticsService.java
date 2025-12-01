package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.repository.CustomerRepository;
import com.XenoTest.Xeno.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public AnalyticsService(OrderRepository orderRepository,
                            CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    // -------- SUMMARY CARD DATA --------
    public Map<String, Object> getSummary(Long tenantId) {

        long totalCustomers = customerRepository.countByTenantId(tenantId);
        long totalOrders = orderRepository.countByTenantId(tenantId);
        Double totalRevenue = orderRepository.sumTotalPriceByTenant(tenantId);

        Map<String, Object> result = new HashMap<>();
        result.put("totalCustomers", totalCustomers);
        result.put("totalOrders", totalOrders);
        result.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);

        return result;
    }

    // -------- ORDERS BY DATE --------
    public List<Map<String, Object>> getOrdersByDate(Long tenantId, String start, String end) {
        return orderRepository.ordersByDate(tenantId, start, end);
    }

    // -------- TOP CUSTOMERS --------
    public List<Map<String, Object>> getTopCustomers(Long tenantId) {
        return orderRepository.topCustomers(tenantId);
    }
}
