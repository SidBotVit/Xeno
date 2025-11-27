package com.XenoTest.Xeno.entity;

public class Tenant {
    private Long id;
    private String shopDomain;
    private String accessToken;
    private String name;

    public Tenant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tenant(Long id, String shopDomain, String accessToken, String name) {
        this.id = id;
        this.shopDomain = shopDomain;
        this.accessToken = accessToken;
        this.name = name;
    }
}
