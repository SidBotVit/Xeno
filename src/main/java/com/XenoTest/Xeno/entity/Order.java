package com.XenoTest.Xeno.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Multi-tenant
    @Column(nullable = false)
    private Long tenantId;

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
    private String customerFirstName;
    private String customerLastName;

    // Address (optional)
    private String addressCity;
    private String addressCountry;
}
