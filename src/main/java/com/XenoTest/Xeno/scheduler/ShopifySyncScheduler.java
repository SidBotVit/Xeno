package com.XenoTest.Xeno.scheduler;

import com.XenoTest.Xeno.service.CustomerSyncService;
import com.XenoTest.Xeno.service.OrderSyncService;
import com.XenoTest.Xeno.service.ProductSyncService;
import com.XenoTest.Xeno.repository.TenantRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ShopifySyncScheduler {

    private final ProductSyncService productSyncService;
    private final OrderSyncService orderSyncService;
    private final CustomerSyncService customerSyncService;
    private final TenantRepository tenantRepository;

    public ShopifySyncScheduler(ProductSyncService productSyncService,
                                OrderSyncService orderSyncService,
                                CustomerSyncService customerSyncService,
                                TenantRepository tenantRepository) {

        this.productSyncService = productSyncService;
        this.orderSyncService = orderSyncService;
        this.customerSyncService = customerSyncService;
        this.tenantRepository = tenantRepository;
    }

    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void autoSyncAllTenants() {

        tenantRepository.findAll().forEach(tenant -> {

            Long tenantId = tenant.getId();
            System.out.println("🔄 Syncing tenant: " + tenant.getName());

            try {
                productSyncService.syncProducts(tenantId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                orderSyncService.syncOrders(tenantId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                customerSyncService.syncCustomers(tenantId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("✔ Sync completed for tenant " + tenant.getName());
        });
    }
}