package com.XenoTest.Xeno.service;


import com.XenoTest.Xeno.entity.Tenant;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TenantService {
    private final Map<Long, Tenant> tenants = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Tenant createTenant(Tenant tenant) {
        long id = idGenerator.getAndIncrement();
        tenant.setId(id);
        tenants.put(id, tenant);
        return tenant;
    }

    public List<Tenant> getAllTenants() {
        return new ArrayList<>(tenants.values());
    }

    public Optional<Tenant> getTenantById(Long id) {
        return Optional.ofNullable(tenants.get(id));
    }

}
