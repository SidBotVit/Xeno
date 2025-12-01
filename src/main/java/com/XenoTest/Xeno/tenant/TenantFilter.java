package com.XenoTest.Xeno.tenant;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String uri = req.getRequestURI();

        // Allow login + signup
        if (uri.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            ((HttpServletResponse) response).sendError(401, "Missing JWT / Tenant");
            return;
        }

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
