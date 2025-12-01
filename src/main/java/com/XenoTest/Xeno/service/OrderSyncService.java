package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.entity.Order;
import com.XenoTest.Xeno.entity.Tenant;
import com.XenoTest.Xeno.repository.OrderRepository;
import com.XenoTest.Xeno.repository.TenantRepository;
import com.XenoTest.Xeno.shopify.ShopifyClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderSyncService {

    private final ShopifyClient shopifyClient;
    private final OrderRepository orderRepo;
    private final TenantRepository tenantRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public OrderSyncService(ShopifyClient shopifyClient,
                            OrderRepository orderRepo,
                            TenantRepository tenantRepo) {
        this.shopifyClient = shopifyClient;
        this.orderRepo = orderRepo;
        this.tenantRepo = tenantRepo;
    }

    // ---------------- SAFE GETTERS ---------------- //

    private String safeText(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v != null && !v.isNull()) ? v.asText() : "";
    }

    private Double safeDouble(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v != null && !v.isNull()) ? v.asDouble() : 0.0;
    }

    private Long safeLong(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v != null && !v.isNull()) ? v.asLong() : 0L;
    }

    private LocalDateTime safeDate(JsonNode node, String field) {
        try {
            JsonNode v = node.get(field);
            if (v == null || v.isNull()) return null;
            String raw = v.asText().replace("Z", "");
            return LocalDateTime.parse(raw, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }

    // ---------------- MAIN SYNC METHOD ---------------- //

    @Transactional
    public String syncOrders(Long tenantId) throws Exception {

        Tenant tenant = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found: " + tenantId));

        String response = shopifyClient
                .getOrders(tenant.getShopDomain(), tenant.getAccessToken())
                .getBody();

        JsonNode root = mapper.readTree(response);
        JsonNode ordersNode = root.get("orders");

        if (ordersNode == null || !ordersNode.isArray()) {
            return "Invalid order response from Shopify";
        }

        for (JsonNode o : ordersNode) {

            Long shopifyOrderId = safeLong(o, "id");

            Order order = orderRepo
                    .findByShopifyOrderIdAndTenantId(shopifyOrderId, tenantId)
                    .orElse(new Order());

            order.setTenantId(tenantId);
            order.setShopifyOrderId(shopifyOrderId);

            // SAFE FIELDS
            order.setName(safeText(o, "name"));
            order.setEmail(safeText(o, "email"));
            order.setFinancialStatus(safeText(o, "financial_status"));
            order.setTotalPrice(safeDouble(o, "total_price"));

            // DATES
            order.setCreatedAt(safeDate(o, "created_at"));
            order.setUpdatedAt(safeDate(o, "updated_at"));

            // CUSTOMER INFO (if exists)
            JsonNode customer = o.get("customer");
            if (customer != null && !customer.isNull()) {
                Long cid = safeLong(customer, "id");   // Shopify customer ID

                order.setCustomerId(cid);

                order.setCustomerFirstName(safeText(customer, "first_name"));
                order.setCustomerLastName(safeText(customer, "last_name"));
            }



            // ADDRESS INFO (if exists)
            JsonNode addr = o.get("billing_address");
            if (addr != null && !addr.isNull()) {
                order.setAddressCity(safeText(addr, "city"));
                order.setAddressCountry(safeText(addr, "country"));
            }

            orderRepo.save(order);
        }

        return "Orders synced successfully for tenant = " + tenantId;
    }
}
