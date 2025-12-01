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

            order.setName(safeText(o, "name"));
            order.setEmail(safeText(o, "email"));
            order.setFinancialStatus(safeText(o, "financial_status"));
            order.setTotalPrice(safeDouble(o, "total_price"));
            order.setCurrency(safeText(o, "currency"));
            order.setOrderStatusUrl(safeText(o, "order_status_url"));

            // Customer data inside order
            JsonNode customerNode = o.get("customer");
            if (customerNode != null && !customerNode.isNull()) {
                order.setCustomerFirstName(safeText(customerNode, "first_name"));
                order.setCustomerLastName(safeText(customerNode, "last_name"));
                // if you have a customerId column, you can also set it here
                // order.setCustomerId(safeLong(customerNode, "id"));
            }

            // Shipping / billing address
            JsonNode shippingAddress = o.get("shipping_address");
            if (shippingAddress != null && !shippingAddress.isNull()) {
                order.setAddressCity(safeText(shippingAddress, "city"));
                order.setAddressCountry(safeText(shippingAddress, "country"));
            } else {
                order.setAddressCity("");
                order.setAddressCountry("");
            }

            orderRepo.save(order);
        }

        return "Orders synced successfully for tenant = " + tenantId;
    }

    // -------- SAFE FIELD HELPERS --------

    private String safeText(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v != null && !v.isNull()) ? v.asText() : "";
    }

    private double safeDouble(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v != null && !v.isNull()) ? v.asDouble() : 0.0;
    }

    private long safeLong(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v != null && !v.isNull()) ? v.asLong() : 0L;
    }
}
