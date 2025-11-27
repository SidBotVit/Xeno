package com.XenoTest.Xeno.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shopDomain;

    @Column(nullable = false)
    private String accessToken;

    private String name;

    public Tenant() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getShopDomain() { return shopDomain; }
    public void setShopDomain(String shopDomain) { this.shopDomain = shopDomain; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
