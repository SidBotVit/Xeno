package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;
import java.util.Map;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Long countByTenantId(Long tenantId);

    List<Customer> findByTenantId(Long tenantId);

    Optional<Customer> findByShopifyCustomerIdAndTenantId(Long shopifyCustomerId, Long tenantId);

    // -------------------- NEW: Customers By Country --------------------
    @Query(value =
            "SELECT country, " +
                    "COUNT(*) AS customers, " +
                    "SUM(CASE WHEN verified_email = true THEN 1 ELSE 0 END) AS verified " +
                    "FROM customers " +
                    "WHERE tenant_id = :tenantId " +
                    "GROUP BY country " +
                    "ORDER BY customers DESC",
            nativeQuery = true)
    List<Map<String, Object>> customersByCountry(@Param("tenantId") Long tenantId);
}
