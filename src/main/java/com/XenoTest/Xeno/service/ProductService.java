package com.XenoTest.Xeno.service;


import com.XenoTest.Xeno.entity.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final Map<Long, Product> products = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);


    public Product addProduct(Product product) {
        long id = idGenerator.getAndIncrement();
        product.setId(id);
        products.put(id, product);
        return product;
    }

    public List<Product> getProductsByTenant(Long tenantId) {
        List<Product> list = new ArrayList<>();
        for (Product p : products.values()) {
            if (p.getTenantId() != null && p.getTenantId().equals(tenantId)) {
                list.add(p);
            }
        }
        return list;
    }

    public Optional<Product> getProduct(Long id, Long tenantId) {
        Product p = products.get(id);
        if (p != null && p.getTenantId().equals(tenantId)) {
            return Optional.of(p);
        }
        return Optional.empty();
    }



}
