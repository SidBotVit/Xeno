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
    @Column(nullable = false)
    private boolean verifiedEmail = false;


    @Column(unique = false)

    private Long shopifyCustomerId;

    private String firstName;
    private String lastName;
    private String email;

    private String phone;
    private String state;
    private String city;
    private String country;

    private String createdAt;
    private String updatedAt;
}
