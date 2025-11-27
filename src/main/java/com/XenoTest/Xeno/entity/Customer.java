package com.XenoTest.Xeno.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shopifyCustomerId;

    private String firstName;
    private String lastName;
    private String email;

    private boolean verifiedEmail;

    private String phone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long ordersCount;
    private Double totalSpent;

    private Long tenantId;  // For multi-tenant isolation
}
