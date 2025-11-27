package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.entity.Customer;
import com.XenoTest.Xeno.repository.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository repo;

    public CustomerController(CustomerRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/db")
    public List<Customer> getCustomersFromDb(@RequestParam Long tenantId) {
        return repo.findByTenantId(tenantId);
    }
}
