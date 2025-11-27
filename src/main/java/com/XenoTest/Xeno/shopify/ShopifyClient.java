package com.XenoTest.Xeno.shopify;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;

@Service
public class ShopifyClient {

    private final RestTemplate restTemplate;

    private static final String API_VERSION = "2022-10";

    public ShopifyClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ------------------------
    // GET PRODUCTS
    // ------------------------
    public ResponseEntity<String> getProducts(String shopDomain, String token) {

        String url = "https://" + shopDomain + "/admin/api/" + API_VERSION + "/products.json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    // ------------------------
    // GET ORDERS
    // ------------------------
    public ResponseEntity<String> getOrders(String shopDomain, String token) {

        String url = "https://" + shopDomain + "/admin/api/" + API_VERSION + "/orders.json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    // ------------------------
    // GET CUSTOMERS
    // ------------------------
    public ResponseEntity<String> getCustomers(String shopDomain, String token) {

        String url = "https://" + shopDomain + "/admin/api/" + API_VERSION + "/customers.json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }
}
