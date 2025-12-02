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

        // 0️⃣ CORS preflight
        if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        // 1️⃣ Static / Frontend routes allowed
        if (
                uri.equals("/") ||
                        uri.equals("/index.html") ||
                        uri.startsWith("/assets") ||
                        uri.endsWith(".js") ||
                        uri.endsWith(".css") ||
                        uri.endsWith(".map") ||
                        uri.endsWith(".png") ||
                        uri.endsWith(".jpg") ||
                        uri.endsWith(".jpeg") ||
                        uri.endsWith(".svg") ||
                        uri.endsWith(".json") ||
                        uri.startsWith("/favicon") ||
                        uri.startsWith("/static") ||
                        uri.startsWith("/css") ||
                        uri.startsWith("/js") ||
                        uri.startsWith("/images")
        ) {
            System.out.println("🔓 PUBLIC ASSET → Allowed");
            chain.doFilter(request, response);
            return;
        }

        // 2️⃣ Auth routes allowed (login if needed)
        if (uri.startsWith("/auth")) {
            System.out.println("🔓 AUTH → Allowed");
            chain.doFilter(request, response);
            return;
        }

        // 3️⃣ Shopify OAuth routes allowed
        if (uri.startsWith("/shopify/install") || uri.startsWith("/shopify/callback")) {
            System.out.println("🔓 SHOPIFY OAUTH → Allowed");
            chain.doFilter(request, response);
            return;
        }

        // 4️⃣ Shopify webhook allowed
        if (uri.startsWith("/webhook/shopify")) {
            System.out.println("🔓 SHOPIFY WEBHOOK → Allowed");
            chain.doFilter(request, response);
            return;
        }

        // 5️⃣ Spring Boot error page allowed
        if (uri.startsWith("/error")) {
            System.out.println("🔓 ERROR PAGE → Allowed");
            chain.doFilter(request, response);
            return;
        }

        // 6️⃣ All other routes require tenant header
        String tenantHeader = req.getHeader("X-Tenant-ID");

        if (tenantHeader == null) {
            System.out.println("❌ BLOCKED — Missing tenant header");
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

        System.out.println("✔ Tenant OK → " + tenantId);
        TenantContext.setTenantId(tenantId);

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
