package com.chaosopen.ddd.application.converter.order;

import com.chaosopen.ddd.client.dto.CreateOrderCmd;
import com.chaosopen.ddd.client.dto.clientobject.CreateOrderCO;
import com.chaosopen.ddd.domain.order.model.Order;
import com.chaosopen.ddd.domain.order.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public final class OrderApplicationConverter {

    private OrderApplicationConverter() {
    }

    public static List<OrderItem> toOrderItems(CreateOrderCmd cmd) {
        List<OrderItem> items = new ArrayList<OrderItem>();
        if (cmd == null || cmd.getItems() == null) {
            return items;
        }
        for (CreateOrderCmd.OrderItemCmd item : cmd.getItems()) {
            items.add(OrderItem.of(item.getSkuId(), item.getSkuName(), item.getQuantity(), item.getUnitPrice()));
        }
        return items;
    }

    public static CreateOrderCO toCreateOrderCO(Order order) {
        return new CreateOrderCO(order.getOrderNo(), order.getStatus().getValue(), order.getStatus().getDesc(),
                order.getTotalAmount());
    }
}
