package com.XenoTest.Xeno.service;

import com.XenoTest.Xeno.entity.Product;
import com.XenoTest.Xeno.entity.Tenant;
import com.XenoTest.Xeno.repository.ProductRepository;
import com.XenoTest.Xeno.repository.TenantRepository;
import com.XenoTest.Xeno.shopify.ShopifyClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductSyncService {

    private final ShopifyClient shopifyClient;
    private final ProductRepository productRepo;
    private final TenantRepository tenantRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public ProductSyncService(ShopifyClient shopifyClient,
                              ProductRepository productRepo,
                              TenantRepository tenantRepo) {

        this.shopifyClient = shopifyClient;
        this.productRepo = productRepo;
        this.tenantRepo = tenantRepo;
    }

    @Transactional
    public String syncProducts(Long tenantId) throws Exception {

        Tenant tenant = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found: " + tenantId));

        String response = shopifyClient
                .getProducts(tenant.getShopDomain(), tenant.getAccessToken())
                .getBody();

        JsonNode root = mapper.readTree(response);
        JsonNode productsNode = root.get("products");

        if (!productsNode.isArray()) {
            return "Invalid product response from Shopify";
        }

        for (JsonNode p : productsNode) {

            Long shopifyId = p.get("id").asLong();

            Product product = productRepo
                    .findByShopifyProductIdAndTenantId(shopifyId, tenantId)
                    .orElse(new Product());

            product.setTenantId(tenantId);
            product.setShopifyProductId(shopifyId);
            product.setTitle(p.get("title").asText());
            product.setVendor(p.get("vendor").asText(""));
            product.setStatus(p.get("status").asText(""));
            product.setHandle(p.get("handle").asText(""));
            product.setDescription(p.get("body_html").asText(""));
            product.setProductType(p.get("product_type").asText(""));

            if (p.get("image") != null && !p.get("image").isNull()) {
                product.setImageUrl(p.get("image").get("src").asText());
            }

            productRepo.save(product);
        }

        return "Products synced successfully for tenant = " + tenantId;
    }
}
