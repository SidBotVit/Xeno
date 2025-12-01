package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.service.CustomerSyncService;
import com.XenoTest.Xeno.service.OrderSyncService;
import com.XenoTest.Xeno.service.ProductSyncService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook/shopify")
public class ShopifyWebhookController {

    private final OrderSyncService orderSyncService;
    private final CustomerSyncService customerSyncService;
    private final ProductSyncService productSyncService;
    private final ObjectMapper objectMapper;

    public ShopifyWebhookController(
            OrderSyncService orderSyncService,
            CustomerSyncService customerSyncService,
            ProductSyncService productSyncService,
            ObjectMapper objectMapper) {
        this.orderSyncService = orderSyncService;
        this.customerSyncService = customerSyncService;
        this.productSyncService = productSyncService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/orders/create")
    public ResponseEntity<String> orderCreated(@RequestHeader("X-Shopify-Shop-Domain") String shop,
                                               @RequestBody String payload) {
        try {
            JsonNode json = objectMapper.readTree(payload);
            System.out.println("📥 Webhook: ORDER CREATED from " + shop);

            orderSyncService.syncSingleOrder(shop, json);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("error");
        }
    }

    @PostMapping("/customers/create")
    public ResponseEntity<String> customerCreated(@RequestHeader("X-Shopify-Shop-Domain") String shop,
                                                  @RequestBody String payload) {
        try {
            JsonNode json = objectMapper.readTree(payload);
            System.out.println("📥 Webhook: CUSTOMER CREATED from " + shop);

            customerSyncService.syncSingleCustomer(shop, json);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("error");
        }
    }

    @PostMapping("/products/create")
    public ResponseEntity<String> productCreated(@RequestHeader("X-Shopify-Shop-Domain") String shop,
                                                 @RequestBody String payload) {
        try {
            JsonNode json = objectMapper.readTree(payload);
            System.out.println("📥 Webhook: PRODUCT CREATED from " + shop);

            productSyncService.syncSingleProduct(shop, json);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("error");
        }
    }
}
