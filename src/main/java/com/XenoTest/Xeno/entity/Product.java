package com.XenoTest.Xeno.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private String title;

    private String shopifyProductId;

    private BigDecimal price;

    public Product() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getShopifyProductId() { return shopifyProductId; }
    public void setShopifyProductId(String shopifyProductId) { this.shopifyProductId = shopifyProductId; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}