package com.XenoTest.Xeno.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    private Double price;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long tenantId; // <-- important for multitenant
}
