package com.XenoTest.Xeno.tenant;

import com.XenoTest.Xeno.entity.Tenant;
import com.XenoTest.Xeno.repository.TenantRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TenantFilter implements Filter {

    private final TenantRepository tenantRepository;

    public TenantFilter(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        System.out.println("🔥 TenantFilter Hit → URI = " + uri);

        // 1️⃣ Webhooks always allowed (NO tenant header)
        if (uri.startsWith("/webhook/shopify")) {
            chain.doFilter(request, response);
            return;
        }

        // 2️⃣ Static resources allowed
        if (uri.endsWith(".html") || uri.endsWith(".js") || uri.endsWith(".css")
                || uri.startsWith("/css") || uri.startsWith("/js")
                || uri.startsWith("/images") || uri.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        // 3️⃣ Auth routes allowed
        if (uri.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        // 4️⃣ Everything else requires tenant
        String tenantHeader = req.getHeader("X-Tenant-ID");

        if (tenantHeader == null) {
            System.out.println("❌ BLOCKED — Missing tenant/token");
            ((HttpServletResponse) response).sendError(403, "Missing tenant header");
            return;
        }

        Long tenantId;
        try {
            tenantId = Long.parseLong(tenantHeader);
        } catch (Exception e) {
            ((HttpServletResponse) response).sendError(400, "Invalid tenant header");
            return;
        }

        Tenant tenant = tenantRepository.findById(tenantId).orElse(null);
        if (tenant == null) {
            ((HttpServletResponse) response).sendError(404, "Tenant not found");
            return;
        }

        // Store tenant in context
        System.out.println("✔ Tenant OK → " + tenantId);
        TenantContext.setTenantId(tenantId);

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
