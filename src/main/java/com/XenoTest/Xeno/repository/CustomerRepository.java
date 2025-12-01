package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByShopifyCustomerIdAndTenantId(Long shopifyCustomerId, Long tenantId);

    List<Customer> findByTenantId(Long tenantId);
}
