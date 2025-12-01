package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.entity.Customer;
import com.XenoTest.Xeno.entity.Tenant;
import com.XenoTest.Xeno.repository.CustomerRepository;
import com.XenoTest.Xeno.repository.TenantRepository;
import com.XenoTest.Xeno.shopify.ShopifyClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerSyncService {

    private final ShopifyClient shopifyClient;
    private final CustomerRepository customerRepo;
    private final TenantRepository tenantRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public CustomerSyncService(ShopifyClient shopifyClient,
                               CustomerRepository customerRepo,
                               TenantRepository tenantRepo) {

        this.shopifyClient = shopifyClient;
        this.customerRepo = customerRepo;
        this.tenantRepo = tenantRepo;
    }

    @Transactional
    public String syncCustomers(Long tenantId) throws Exception {

        Tenant tenant = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found: " + tenantId));

        String response = shopifyClient
                .getCustomers(tenant.getShopDomain(), tenant.getAccessToken())
                .getBody();

        JsonNode root = mapper.readTree(response);
        JsonNode customersNode = root.get("customers");

        if (customersNode == null || !customersNode.isArray()) {
            return "Invalid customer response from Shopify";
        }

        for (JsonNode c : customersNode) {

            Long shopifyCustomerId = safeLong(c, "id");

            Customer customer = customerRepo
                    .findByShopifyCustomerIdAndTenantId(shopifyCustomerId, tenantId)
                    .orElse(new Customer());

            customer.setTenantId(tenantId);
            customer.setShopifyCustomerId(shopifyCustomerId);

            customer.setFirstName(safeText(c, "first_name"));
            customer.setLastName(safeText(c, "last_name"));
            customer.setEmail(safeText(c, "email"));
            customer.setPhone(safeText(c, "phone"));

            // verified_email is NOT NULL in DB → must always set
            customer.setVerifiedEmail(safeBoolean(c, "verified_email"));

            JsonNode defaultAddress = c.get("default_address");
            if (defaultAddress != null && !defaultAddress.isNull()) {
                customer.setCity(safeText(defaultAddress, "city"));
                customer.setCountry(safeText(defaultAddress, "country"));
                customer.setState(safeText(defaultAddress, "province"));
            } else {
                customer.setCity("");
                customer.setCountry("");
                customer.setState("");
            }

            customerRepo.save(customer);
        }

        return "Customers synced successfully for tenant = " + tenantId;
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

    private boolean safeBoolean(JsonNode node, String field) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) return false;
        if (v.isBoolean()) return v.asBoolean();
        if (v.isTextual()) return Boolean.parseBoolean(v.asText());
        return false;
    }
}
