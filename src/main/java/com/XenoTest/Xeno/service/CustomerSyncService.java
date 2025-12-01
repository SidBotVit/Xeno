package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.entity.Customer;
import com.XenoTest.Xeno.entity.Tenant;
import com.XenoTest.Xeno.repository.CustomerRepository;
import com.XenoTest.Xeno.repository.TenantRepository;
import com.XenoTest.Xeno.shopify.ShopifyClient;
import com.XenoTest.Xeno.tenant.TenantContext;
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
    public String syncCustomers() throws Exception {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) return "Missing tenant header 'X-Tenant-ID'";

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

            Long shopifyCustomerId = c.get("id").asLong();

            Customer customer =
                    customerRepo.findByShopifyCustomerIdAndTenantId(shopifyCustomerId, tenantId)
                            .orElse(new Customer());

            customer.setTenantId(tenantId);
            customer.setShopifyCustomerId(shopifyCustomerId);
            customer.setFirstName(c.get("first_name").asText(""));
            customer.setLastName(c.get("last_name").asText(""));
            customer.setEmail(c.get("email").asText(""));
            customer.setPhone(c.get("phone").asText(""));
            customer.setState(c.get("state").asText(""));
            customer.setCountry(c.get("country").asText(""));
            customer.setCurrency(c.get("currency").asText(""));
            customer.setCreatedAt(c.get("created_at").asText(""));
            customer.setUpdatedAt(c.get("updated_at").asText(""));

            customerRepo.save(customer);
        }

        return "Customers synced successfully for tenant = " + tenantId;
    }
}
