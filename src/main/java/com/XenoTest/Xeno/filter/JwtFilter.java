package com.XenoTest.Xeno.filter;

import com.XenoTest.Xeno.security.JwtUtil;
import com.XenoTest.Xeno.tenant.TenantContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = JwtUtil.validateToken(token);

                Long tenantId = claims.get("tenantId", Long.class);
                String email = claims.getSubject(); // email stored in JWT

                // store tenant for DB layer
                TenantContext.setTenantId(tenantId);

                // 🔥 mark user as authenticated
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, List.of());

                SecurityContextHolder.getContext().setAuthentication(auth);

                System.out.println("🔑 JWT Filter tenant=" + tenantId);

            } catch (Exception e) {
                ((HttpServletResponse) response).sendError(401, "Invalid token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
