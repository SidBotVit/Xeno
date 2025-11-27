package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.entity.Customer;
import com.XenoTest.Xeno.repository.CustomerRepository;
import com.XenoTest.Xeno.shopify.ShopifyClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerSyncService {

    private final ShopifyClient shopifyClient;
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomerSyncService(ShopifyClient shopifyClient, CustomerRepository customerRepository) {
        this.shopifyClient = shopifyClient;
        this.customerRepository = customerRepository;
    }

    public String syncCustomers(Long tenantId, String shopDomain, String token) {
        try {
            ResponseEntity<String> response = shopifyClient.getCustomers(shopDomain, token);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode customers = root.get("customers");

            for (JsonNode c : customers) {

                Long shopifyId = c.get("id").asLong();
                String firstName = c.get("first_name").asText("");
                String lastName = c.get("last_name").asText("");
                String email = c.get("email").asText("");
                boolean verified = c.get("verified_email").asBoolean(false);
                String phone = c.has("phone") && !c.get("phone").isNull()
                        ? c.get("phone").asText()
                        : null;

                Long ordersCount = c.has("orders_count") ? c.get("orders_count").asLong() : 0;
                Double totalSpent = c.has("total_spent") ? c.get("total_spent").asDouble() : 0.0;

                Customer existing = customerRepository
                        .findByShopifyCustomerIdAndTenantId(shopifyId, tenantId);

                Customer customer = existing != null ? existing : new Customer();

                customer.setShopifyCustomerId(shopifyId);
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setEmail(email);
                customer.setVerifiedEmail(verified);
                customer.setPhone(phone);
                customer.setOrdersCount(ordersCount);
                customer.setTotalSpent(totalSpent);

                customer.setTenantId(tenantId);

                customer.setCreatedAt(LocalDateTime.now());
                customer.setUpdatedAt(LocalDateTime.now());

                customerRepository.save(customer);
            }

            return "Customers synced successfully for tenantId = " + tenantId;

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to sync customers: " + e.getMessage();
        }
    }
}
