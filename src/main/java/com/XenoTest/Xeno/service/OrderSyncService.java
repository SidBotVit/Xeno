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

import java.time.OffsetDateTime;
import java.time.LocalDateTime;

@Service
public class OrderSyncService {

    private final ShopifyClient shopifyClient;
    private final OrderRepository orderRepo;
    private final TenantRepository tenantRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public OrderSyncService(
            ShopifyClient shopifyClient,
            OrderRepository orderRepo,
            TenantRepository tenantRepo
    ) {
        this.shopifyClient = shopifyClient;
        this.orderRepo = orderRepo;
        this.tenantRepo = tenantRepo;
    }

    // ---------------- SAFE HELPERS ---------------- //
    private String safeText(JsonNode n, String f) {
        return (n.has(f) && !n.get(f).isNull()) ? n.get(f).asText() : "";
    }

    private Double safeDouble(JsonNode n, String f) {
        return (n.has(f) && !n.get(f).isNull()) ? n.get(f).asDouble() : 0.0;
    }

    private Long safeLong(JsonNode n, String f) {
        return (n.has(f) && !n.get(f).isNull()) ? n.get(f).asLong() : 0L;
    }

    private LocalDateTime safeDate(JsonNode n, String f) {
        try {
            if (!n.has(f) || n.get(f).isNull()) return null;
            return OffsetDateTime.parse(n.get(f).asText()).toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    // ---------------- WEBHOOK SYNC ---------------- //
    public void syncSingleOrder(String shopDomain, JsonNode json) {

        Tenant tenant = tenantRepo.findByShopDomain(shopDomain);
        if (tenant == null) {
            System.out.println("❌ Unknown tenant for shop: " + shopDomain);
            return;
        }

        Long tenantId = tenant.getId();
        Long shopifyOrderId = json.get("id").asLong();

        Order order = orderRepo
                .findByShopifyOrderIdAndTenantId(shopifyOrderId, tenantId)
                .orElse(new Order());

        order.setTenantId(tenantId);
        order.setShopifyOrderId(shopifyOrderId);

        order.setEmail(safeText(json, "email"));
        order.setTotalPrice(safeDouble(json, "total_price"));
        order.setCreatedAt(safeDate(json, "created_at"));
        order.setUpdatedAt(safeDate(json, "updated_at"));
        order.setName(safeText(json, "name"));
        order.setFinancialStatus(safeText(json, "financial_status"));

        // CUSTOMER
        JsonNode cust = json.get("customer");
        if (cust != null && !cust.isNull()) {
            order.setCustomerId(safeLong(cust, "id"));
            order.setCustomerFirstName(safeText(cust, "first_name"));
            order.setCustomerLastName(safeText(cust, "last_name"));
        }

        // ADDRESS
        JsonNode addr = json.get("billing_address");
        if (addr != null && !addr.isNull()) {
            order.setAddressCity(safeText(addr, "city"));
            order.setAddressCountry(safeText(addr, "country"));
        }

        orderRepo.save(order);

        System.out.println("✅ Saved webhook order " + shopifyOrderId + " for tenant " + tenantId);
    }

    // ---------------- FULL SYNC ---------------- //
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
            return "Invalid Shopify response";
        }

        for (JsonNode o : ordersNode) {

            Long shopifyOrderId = safeLong(o, "id");

            Order order = orderRepo
                    .findByShopifyOrderIdAndTenantId(shopifyOrderId, tenantId)
                    .orElse(new Order());

            order.setTenantId(tenantId);
            order.setShopifyOrderId(shopifyOrderId);

            order.setName(safeText(o, "name"));
            order.setEmail(safeText(o, "email"));
            order.setFinancialStatus(safeText(o, "financial_status"));
            order.setTotalPrice(safeDouble(o, "total_price"));

            order.setCreatedAt(safeDate(o, "created_at"));
            order.setUpdatedAt(safeDate(o, "updated_at"));

            JsonNode customer = o.get("customer");
            if (customer != null && !customer.isNull()) {
                order.setCustomerId(safeLong(customer, "id"));
                order.setCustomerFirstName(safeText(customer, "first_name"));
                order.setCustomerLastName(safeText(customer, "last_name"));
            }

            JsonNode addr = o.get("billing_address");
            if (addr != null && !addr.isNull()) {
                order.setAddressCity(safeText(addr, "city"));
                order.setAddressCountry(safeText(addr, "country"));
            }

            orderRepo.save(order);
        }

        return "Orders synced for tenant " + tenantId;
    }
}
