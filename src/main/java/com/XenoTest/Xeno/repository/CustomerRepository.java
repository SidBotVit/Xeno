package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Long countByTenantId(Long tenantId);

    List<Customer> findByTenantId(Long tenantId);

    Optional<Customer> findByShopifyCustomerIdAndTenantId(Long shopifyCustomerId, Long tenantId);
}
