package com.chaosopen.ddd.domain.order.model;

import com.chaosopen.ddd.common.exception.BizException;
import com.chaosopen.ddd.domain.order.enums.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class OrderBehaviorTest {

    @Test
    void should_submit_pay_ship_successfully() {
        Order order = Order.init(1L, 10L);
        order.addItem(OrderItem.of(100L, "sku-a", 2, new BigDecimal("12.50")));

        order.submit();
        order.pay();
        order.ship();

        Assertions.assertEquals(OrderStatus.SHIPPED, order.getStatus());
        Assertions.assertEquals(new BigDecimal("25.00"), order.getTotalAmount());
    }

    @Test
    void should_fail_when_submit_empty_order() {
        Order order = Order.init(1L, 10L);

        Assertions.assertThrows(BizException.class, order::submit);
    }

    @Test
    void should_fail_when_pay_before_submit() {
        Order order = Order.init(1L, 10L);

        Assertions.assertThrows(BizException.class, order::pay);
    }
}
