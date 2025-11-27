package com.XenoTest.Xeno.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Long tenantId;             // which store
    private String shopifyOrderId;     // Shopify order id
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<String> productIds;

    public Order(Long id, Long tenantId, String shopifyOrderId, BigDecimal totalAmount, LocalDateTime createdAt, List<String> productIds) {
        this.id = id;
        this.tenantId = tenantId;
        this.shopifyOrderId = shopifyOrderId;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.productIds = productIds;
    }

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getShopifyOrderId() {
        return shopifyOrderId;
    }

    public void setShopifyOrderId(String shopifyOrderId) {
        this.shopifyOrderId = shopifyOrderId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}
