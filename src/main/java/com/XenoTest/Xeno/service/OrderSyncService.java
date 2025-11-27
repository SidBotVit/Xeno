package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.entity.Order;
import com.XenoTest.Xeno.entity.Tenant;
import com.XenoTest.Xeno.repository.OrderRepository;
import com.XenoTest.Xeno.repository.TenantRepository;
import com.XenoTest.Xeno.shopify.ShopifyClient;
import com.XenoTest.Xeno.tenant.TenantContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public String syncOrders() throws Exception {

        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) return "Missing tenant header 'X-Tenant-ID'";

        Tenant tenant = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found: " + tenantId));

        String response = shopifyClient
                .getOrders(tenant.getShopDomain(), tenant.getAccessToken())
                .getBody();

        JsonNode root = mapper.readTree(response);
        JsonNode ordersNode = root.get("orders");

        if (ordersNode == null || !ordersNode.isArray())
            return "Invalid order response from Shopify";

        for (JsonNode o : ordersNode) {

            Long shopifyOrderId = o.get("id").asLong();

            Order order =
                    orderRepo.findByShopifyOrderIdAndTenantId(shopifyOrderId, tenantId)
                            .orElse(new Order());

            order.setTenantId(tenantId);
            order.setShopifyOrderId(shopifyOrderId);

            order.setName(o.get("name").asText(""));
            order.setEmail(o.get("email").asText(""));

            order.setFinancialStatus(o.get("financial_status").asText(""));
            order.setFulfillmentStatus(o.get("fulfillment_status").asText(""));
            order.setCurrency(o.get("currency").asText(""));

            order.setTotalPrice(o.get("total_price").asDouble());

            order.setCreatedAt(o.get("created_at") != null ?
                    LocalDateTime.parse(o.get("created_at").asText().replace("Z", "")) : null);

            order.setUpdatedAt(o.get("updated_at") != null ?
                    LocalDateTime.parse(o.get("updated_at").asText().replace("Z", "")) : null);

            JsonNode cust = o.get("customer");
            if (cust != null && !cust.isNull()) {
                order.setCustomerFirstName(cust.get("first_name").asText(""));
                order.setCustomerLastName(cust.get("last_name").asText(""));
            }

            order.setOrderStatusUrl(o.get("order_status_url").asText(""));

            orderRepo.save(order);
        }

        return "Orders synced successfully for tenant = " + tenantId;
    }
}
