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

    // ---------------- WEBHOOK: SINGLE PRODUCT ----------------
    public void syncSingleProduct(String shopDomain, JsonNode json) {

        Tenant tenant = tenantRepo.findByShopDomain(shopDomain);

        if (tenant == null) {
            System.out.println("❌ Unknown tenant for shop: " + shopDomain);
            return;
        }

        Long tenantId = tenant.getId();
        Long shopifyProductId = safeLong(json, "id");

        Product product = productRepo
                .findByShopifyProductIdAndTenantId(shopifyProductId, tenantId)
                .orElse(new Product());

        product.setTenantId(tenantId);
        product.setShopifyProductId(shopifyProductId);

        product.setTitle(safeText(json, "title"));
        product.setVendor(safeText(json, "vendor"));
        product.setStatus(safeText(json, "status"));
        product.setHandle(safeText(json, "handle"));
        product.setDescription(safeText(json, "body_html"));
        product.setProductType(safeText(json, "product_type"));

        // IMAGE
        JsonNode imageNode = json.get("image");
        if (imageNode != null && !imageNode.isNull()) {
            product.setImageUrl(safeText(imageNode, "src"));
        }

        // VARIANTS (price, sku from first variant if exists)
        JsonNode variants = json.get("variants");
        if (variants != null && variants.isArray() && variants.size() > 0) {
            JsonNode v = variants.get(0);

            product.setPrice(safeDouble(v, "price"));
            product.setSku(safeText(v, "sku"));
        }

        productRepo.save(product);

        System.out.println("✅ Single product synced: " + shopifyProductId);
    }

    // ---------------- BULK SYNC (Scheduler / Manual) ----------------
    @Transactional
    public String syncProducts(Long tenantId) throws Exception {

        Tenant tenant = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found: " + tenantId));

        String response = shopifyClient
                .getProducts(tenant.getShopDomain(), tenant.getAccessToken())
                .getBody();

        JsonNode root = mapper.readTree(response);
        JsonNode productsNode = root.get("products");

        if (productsNode == null || !productsNode.isArray()) {
            return "Invalid product response from Shopify";
        }

        for (JsonNode p : productsNode) {

            Long shopifyId = safeLong(p, "id");

            Product product = productRepo
                    .findByShopifyProductIdAndTenantId(shopifyId, tenantId)
                    .orElse(new Product());

            product.setTenantId(tenantId);
            product.setShopifyProductId(shopifyId);
            product.setTitle(safeText(p, "title"));
            product.setVendor(safeText(p, "vendor"));
            product.setStatus(safeText(p, "status"));
            product.setHandle(safeText(p, "handle"));
            product.setDescription(safeText(p, "body_html"));
            product.setProductType(safeText(p, "product_type"));

            // IMAGE
            JsonNode image = p.get("image");
            if (image != null && !image.isNull()) {
                product.setImageUrl(safeText(image, "src"));
            }

            // VARIANTS (price, sku from first variant)
            JsonNode variants = p.get("variants");
            if (variants != null && variants.isArray() && variants.size() > 0) {
                JsonNode v = variants.get(0);
                product.setPrice(safeDouble(v, "price"));
                product.setSku(safeText(v, "sku"));
            }

            productRepo.save(product);
        }

        return "Products synced successfully for tenant = " + tenantId;
    }

    // ---------------- SAFE HELPERS ----------------
    private String safeText(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v != null && !v.isNull()) ? v.asText() : "";
    }

    private double safeDouble(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v != null && !v.isNull()) ? v.asDouble() : 0.0;
    }

    private long safeLong(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return (v != null && !v.isNull()) ? v.asLong() : 0L;
    }
}
