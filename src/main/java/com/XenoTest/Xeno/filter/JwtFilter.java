package com.XenoTest.Xeno.filter;

import com.XenoTest.Xeno.security.JwtUtil;
import com.XenoTest.Xeno.tenant.TenantContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws java.io.IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = JwtUtil.validateToken(token);
                Long tenantId = claims.get("tenantId", Long.class);

                // set tenant
                TenantContext.setTenantId(tenantId);


            } catch (Exception e) {
                ((HttpServletResponse) response).sendError(401, "Invalid token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
