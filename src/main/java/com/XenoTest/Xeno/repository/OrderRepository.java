package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByTenantId(Long tenantId);

    Optional<Order> findByShopifyOrderIdAndTenantId(Long shopifyOrderId, Long tenantId);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.tenantId = :tenantId")
    Double sumTotalPriceByTenant(@Param("tenantId") Long tenantId);

    Long countByTenantId(Long tenantId);

    // -------------------- EXISTING --------------------
    @Query(value =
            "SELECT DATE(created_at) AS date, COUNT(*) AS orders, SUM(total_price) AS revenue " +
                    "FROM orders " +
                    "WHERE tenant_id = :tenantId " +
                    "AND created_at >= :start " +
                    "AND created_at <= :end " +
                    "GROUP BY DATE(created_at) " +
                    "ORDER BY DATE(created_at)",
            nativeQuery = true)
    List<Map<String, Object>> ordersByDate(
            @Param("tenantId") Long tenantId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    // -------------------- EXISTING --------------------
    @Query(value =
            "SELECT c.first_name AS name, c.email AS email, SUM(o.total_price) AS totalSpent " +
                    "FROM orders o " +
                    "JOIN customers c ON o.customer_id = c.shopify_customer_id " +
                    "WHERE o.tenant_id = :tenantId " +
                    "GROUP BY c.first_name, c.email " +
                    "ORDER BY totalSpent DESC " +
                    "LIMIT 5",
            nativeQuery = true)
    List<Map<String, Object>> topCustomers(@Param("tenantId") Long tenantId);

    // -------------------- NEW: Revenue By Country --------------------
    @Query(value =
            "SELECT address_country AS country, " +
                    "COUNT(*) AS orders, " +
                    "SUM(total_price) AS revenue " +
                    "FROM orders " +
                    "WHERE tenant_id = :tenantId " +
                    "GROUP BY address_country " +
                    "ORDER BY revenue DESC",
            nativeQuery = true)
    List<Map<String, Object>> revenueByCountry(@Param("tenantId") Long tenantId);

    // -------------------- NEW: New vs Returning Customers --------------------
    @Query(value =
            "SELECT " +
                    "CASE WHEN sub.order_count = 1 THEN 'new' ELSE 'returning' END AS segment, " +
                    "COUNT(*) AS customerCount, " +
                    "SUM(sub.total_spent) AS revenue " +
                    "FROM ( " +
                    "   SELECT customer_id, COUNT(*) AS order_count, SUM(total_price) AS total_spent " +
                    "   FROM orders " +
                    "   WHERE tenant_id = :tenantId AND customer_id IS NOT NULL " +
                    "   GROUP BY customer_id " +
                    ") sub " +
                    "GROUP BY segment",
            nativeQuery = true)
    List<Map<String, Object>> newVsReturning(@Param("tenantId") Long tenantId);
}
