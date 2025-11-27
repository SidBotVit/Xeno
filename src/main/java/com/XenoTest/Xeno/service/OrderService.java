package com.XenoTest.Xeno.service;


import com.XenoTest.Xeno.entity.Order;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

        private final Map<Long, Order> orders = new HashMap<>();
        private final AtomicLong idGenerator = new AtomicLong(1);

        public Order addOrder(Order order) {
            order.setId(idGenerator.getAndIncrement());
            orders.put(order.getId(), order);
            return order;
        }

        public List<Order> getOrdersForTenant(Long tenantId) {
            List<Order> list = new ArrayList<>();
            for (Order o : orders.values()) {
                if (o.getTenantId().equals(tenantId)) {
                    list.add(o);
                }
            }
            return list;
        }
}
