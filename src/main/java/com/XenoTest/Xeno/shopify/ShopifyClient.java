package com.XenoTest.Xeno.shopify;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class ShopifyClient {

    private final RestTemplate restTemplate;
    private static final String API_VERSION = "2022-10";

    public ShopifyClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 🔥 Generic method to call Shopify API
    private ResponseEntity<String> callShopifyAPI(String shopDomain, String token, String endpoint) {

        String url = "https://" + shopDomain + "/admin/api/" + API_VERSION + "/" + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println("🔥 Shopify API Error (" + endpoint + "): " + e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
            throw e;
        }
    }

    // 🔵 Get all products
    public ResponseEntity<String> getProducts(String shopDomain, String token) {
        return callShopifyAPI(shopDomain, token, "products.json");
    }

    // 🟢 Get all orders
    public ResponseEntity<String> getOrders(String shopDomain, String token) {
        return callShopifyAPI(shopDomain, token, "orders.json");
    }

    // 🟡 Get all customers
    public ResponseEntity<String> getCustomers(String shopDomain, String token) {
        return callShopifyAPI(shopDomain, token, "customers.json");
    }
}
