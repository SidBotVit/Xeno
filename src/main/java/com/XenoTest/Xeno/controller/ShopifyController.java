package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.shopify.ShopifyClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopify")
public class ShopifyController {

    private final ShopifyClient shopifyClient;

    public ShopifyController(ShopifyClient shopifyClient) {
        this.shopifyClient = shopifyClient;
    }

    @GetMapping("/products")
    public ResponseEntity<String> getProducts(
            @RequestParam String shopDomain,
            @RequestParam String token
    ) {
        return shopifyClient.getProducts(shopDomain, token);
    }

    @GetMapping("/orders")
    public ResponseEntity<String> getOrders(
            @RequestParam String shopDomain,
            @RequestParam String token
    ) {
        return shopifyClient.getOrders(shopDomain, token);
    }

    @GetMapping("/customers")
    public ResponseEntity<String> getCustomers(
            @RequestParam String shopDomain,
            @RequestParam String token
    ) {
        return shopifyClient.getCustomers(shopDomain, token);
    }
}
