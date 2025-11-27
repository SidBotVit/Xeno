package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Tenant findByShopDomain(String shopDomain);
}