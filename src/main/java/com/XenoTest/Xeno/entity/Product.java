package com.XenoTest.Xeno.entity;

import java.math.BigDecimal;

public class Product {
    private Long id;        // internal id
    private Long tenantId;  // which store this belongs to
    private String title;
    private String shopifyProductId; // Shopify product id as string
    private BigDecimal price;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShopifyProductId() {
        return shopifyProductId;
    }

    public void setShopifyProductId(String shopifyProductId) {
        this.shopifyProductId = shopifyProductId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Product(Long id, Long tenantId, String title, String shopifyProductId, BigDecimal price) {
        this.id = id;
        this.tenantId = tenantId;
        this.title = title;
        this.shopifyProductId = shopifyProductId;
        this.price = price;
    }

    public Product() {
    }
}
