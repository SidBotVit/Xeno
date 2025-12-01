package com.XenoTest.Xeno.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String tenantHeader = request.getHeader("X-Tenant-ID");
        System.out.println("🔎 Received Tenant Header = " + tenantHeader);

        if (tenantHeader == null) {
            response.setStatus(400);
            response.getWriter().write("Missing tenant header 'X-Tenant-ID'");
            return false;
        }

        try {
            Long tenantId = Long.parseLong(tenantHeader);
            TenantContext.setTenantId(tenantId);
            return true;

        } catch (Exception e) {
            response.setStatus(400);
            response.getWriter().write("Invalid tenant id in header");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        TenantContext.clear();
    }
}
