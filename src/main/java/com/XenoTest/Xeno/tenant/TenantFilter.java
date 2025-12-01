package com.XenoTest.Xeno.tenant;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        System.out.println("🔥 TenantFilter Hit → URI = " + uri);

        // ALLOW AUTH
        if (uri.startsWith("/auth")) {
            System.out.println("✔ ALLOW /auth → " + uri);
            chain.doFilter(request, response);
            return;
        }

        // ALLOW STATIC FILES
        if (uri.endsWith(".html") ||
                uri.endsWith(".css") ||
                uri.endsWith(".js") ||
                uri.endsWith(".png") ||
                uri.endsWith(".jpg") ||
                uri.startsWith("/static")) {

            System.out.println("✔ ALLOW STATIC → " + uri);
            chain.doFilter(request, response);
            return;
        }

        // Protected APIs → NEED TENANT
        Long tenantId = TenantContext.getTenantId();
        System.out.println("🔎 Tenant inside filter = " + tenantId);
        System.out.println("🏷 TenantFilter tenant=" + TenantContext.getTenantId());


        if (tenantId == null) {
            System.out.println("❌ BLOCKED — Missing tenant/token");
            ((HttpServletResponse) response).sendError(401, "Missing JWT / Tenant");
            return;
        }

        try {
            System.out.println("✔ Tenant OK → " + tenantId);
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
