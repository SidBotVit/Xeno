package com.XenoTest.Xeno.tenant;

import com.XenoTest.Xeno.entity.Tenant;
import com.XenoTest.Xeno.repository.TenantRepository;
import org.springframework.stereotype.Service;

@Service
public class TenantResolverService {

    private final TenantRepository tenantRepository;

    public TenantResolverService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public Tenant resolveTenant(String shopDomain) {
        return tenantRepository.findByShopDomain(shopDomain);
    }
}
