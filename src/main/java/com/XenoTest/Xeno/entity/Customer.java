package com.XenoTest.Xeno.entity;

import jakarta.persistence.*;

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
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Long getShopifyCustomerId() { return shopifyCustomerId; }
    public void setShopifyCustomerId(Long shopifyCustomerId) { this.shopifyCustomerId = shopifyCustomerId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isVerifiedEmail() { return verifiedEmail; }
    public void setVerifiedEmail(boolean verifiedEmail) { this.verifiedEmail = verifiedEmail; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

}
