package com.XenoTest.Xeno.tenant;

import com.XenoTest.Xeno.entity.Tenant;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TenantFilter implements Filter {

    private final TenantResolverService tenantResolver;

    public TenantFilter(TenantResolverService tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;

        String shopDomain = http.getHeader("X-Shop-Domain");

        if (shopDomain != null) {
            Tenant tenant = tenantResolver.resolveTenant(shopDomain);

            if (tenant != null) {
                TenantContext.setTenantId(tenant.getId());
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
