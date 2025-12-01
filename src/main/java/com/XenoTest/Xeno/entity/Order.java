package com.XenoTest.Xeno.entity;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "orders")


public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Multi-tenant
    @Column(nullable = false)
    private Long tenantId;
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_first_name")
    private String customerFirstName;

    @Column(name = "customer_last_name")
    private String customerLastName;


    // Shopify order ID
    @Column(nullable = false)
    private Long shopifyOrderId;

    private String name;                // "#1001"
    private String email;               // customer email
    private String financialStatus;     // paid, pending, refunded
    private Double totalPrice;          // price

    private String currency;            // "USD"
    private String orderStatusUrl;      // link

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Customer info (optional)

    // Address (optional)
    private String addressCity;
    private String addressCountry;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Long getShopifyOrderId() { return shopifyOrderId; }
    public void setShopifyOrderId(Long shopifyOrderId) { this.shopifyOrderId = shopifyOrderId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFinancialStatus() { return financialStatus; }
    public void setFinancialStatus(String financialStatus) { this.financialStatus = financialStatus; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerFirstName() { return customerFirstName; }
    public void setCustomerFirstName(String customerFirstName) { this.customerFirstName = customerFirstName; }

    public String getCustomerLastName() { return customerLastName; }
    public void setCustomerLastName(String customerLastName) { this.customerLastName = customerLastName; }

    public String getAddressCity() { return addressCity; }
    public void setAddressCity(String addressCity) { this.addressCity = addressCity; }

    public String getAddressCountry() { return addressCountry; }
    public void setAddressCountry(String addressCountry) { this.addressCountry = addressCountry; }

}
