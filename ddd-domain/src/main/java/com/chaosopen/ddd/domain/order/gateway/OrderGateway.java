package com.chaosopen.ddd.domain.order.gateway;

import com.chaosopen.ddd.domain.order.model.Order;

import java.util.Optional;

public interface OrderGateway {

    void save(Order order);

    Optional<Order> findByOrderNo(String orderNo);
}
