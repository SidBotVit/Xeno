package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.entity.Customer;
import com.XenoTest.Xeno.repository.CustomerRepository;
import com.XenoTest.Xeno.tenant.TenantContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerViewController {

    private final CustomerRepository repo;

    public CustomerViewController(CustomerRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Customer> getCustomers() {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null)
            throw new RuntimeException("Missing X-Tenant-ID");

        return repo.findByTenantId(tenantId);
    }
}
