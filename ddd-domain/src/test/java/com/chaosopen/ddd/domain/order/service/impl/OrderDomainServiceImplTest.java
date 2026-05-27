package com.chaosopen.ddd.domain.order.service.impl;

import com.chaosopen.ddd.domain.order.dto.CreateOrderResult;
import com.chaosopen.ddd.domain.order.event.OrderCreatedDomainEvent;
import com.chaosopen.ddd.domain.order.gateway.OrderGateway;
import com.chaosopen.ddd.domain.order.model.Order;
import com.chaosopen.ddd.domain.order.model.OrderItem;
import com.chaosopen.ddd.domain.product.model.Sku;
import com.chaosopen.ddd.domain.store.model.Store;
import com.chaosopen.ddd.domain.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OrderDomainServiceImplTest {

    @Test
    void should_register_order_created_event_in_aggregate() {
        OrderDomainServiceImpl service = new OrderDomainServiceImpl();
        service.setOrderGateway(new InMemoryOrderGateway());

        OrderItem item = OrderItem.of(1001L, "sku-1", 2, new BigDecimal("18.80"));
        User user = new User();
        user.setUserId(9L);
        user.setMobile("13800000000");
        Store store = new Store();
        store.setStoreId(88L);
        Sku sku = new Sku();
        sku.setSkuId(1001L);
        sku.setSkuName("sku-1");
        sku.setSalePrice(new BigDecimal("18.80"));
        Map<Long, Sku> skuMap = new HashMap<Long, Sku>();
        skuMap.put(1001L, sku);

        CreateOrderResult result = service.placeOrder(Arrays.asList(item), user, store, skuMap);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getOrder());
        Assertions.assertEquals(2, result.getOrderTotalQuantity().intValue());
        List<Object> domainEvents = result.getOrder().pullDomainEvents();
        Assertions.assertEquals(1, domainEvents.size());
        Assertions.assertTrue(domainEvents.get(0) instanceof OrderCreatedDomainEvent);
        Assertions.assertTrue(result.getOrder().pullDomainEvents().isEmpty());
    }

    private static class InMemoryOrderGateway implements OrderGateway {
        @Override
        public void save(Order order) {
            order.setOrderId(100L);
        }

        @Override
        public java.util.Optional<Order> findByOrderNo(String orderNo) {
            return java.util.Optional.empty();
        }
    }
}
