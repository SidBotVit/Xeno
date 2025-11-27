package com.XenoTest.Xeno.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    @Column(nullable = false, unique = false)
    private Long shopifyOrderId;

    private String name; // Shopify order name => "#1001"
    private String email;

    private String financialStatus;  // paid / pending / refunded
    private String fulfillmentStatus; // fulfilled / unfulfilled
    private Double totalPrice;

    private String currency;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String customerFirstName;
    private String customerLastName;

    private String orderStatusUrl; // Shopify order status page

    // ---------- GETTERS & SETTERS ----------
    // (Generate using IDE)
}
