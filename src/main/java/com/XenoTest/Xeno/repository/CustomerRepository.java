package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByTenantId(Long tenantId);

    Customer findByShopifyCustomerIdAndTenantId(Long shopifyCustomerId, Long tenantId);
}
