package com.XenoTest.Xeno.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private Long shopifyCustomerId;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private String state;
    private String country;

    private String currency;

    private String createdAt;
    private String updatedAt;
}
