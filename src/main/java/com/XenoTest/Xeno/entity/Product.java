package com.XenoTest.Xeno.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shopifyProductId;

    private String title;
    private String description;
    private String vendor;
    private String productType;

    private String status;
    private String handle;
    private String imageUrl;

    private Double price;  // <-- Price exists but no getter/setter earlier

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long tenantId;
    private String sku;


    // ------------------- GETTERS & SETTERS -------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Long getShopifyProductId() { return shopifyProductId; }
    public void setShopifyProductId(Long shopifyProductId) { this.shopifyProductId = shopifyProductId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getHandle() { return handle; }
    public void setHandle(String handle) { this.handle = handle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // ⭐️ ADD THESE ⭐️
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
