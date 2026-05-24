package com.chaosopen.ddd.infrastructure.order.converter;

import com.chaosopen.ddd.domain.order.enums.OrderStatus;
import com.chaosopen.ddd.domain.order.model.Order;
import com.chaosopen.ddd.domain.order.model.OrderItem;
import com.chaosopen.ddd.infrastructure.order.persistence.dataobject.OrderDO;
import com.chaosopen.ddd.infrastructure.order.persistence.dataobject.OrderItemDO;

import java.util.ArrayList;
import java.util.List;

public final class OrderInfrastructureConverter {

    private OrderInfrastructureConverter() {
    }

    public static OrderDO toOrderDO(Order order) {
        OrderDO orderDO = new OrderDO();
        orderDO.setOrderId(order.getOrderId());
        orderDO.setOrderNo(order.getOrderNo());
        orderDO.setUserId(order.getUserId());
        orderDO.setStoreId(order.getStoreId());
        orderDO.setStatus(order.getStatus() == null ? null : order.getStatus().getValue());
        orderDO.setTotalAmount(order.getTotalAmount());
        orderDO.setCreatedAt(order.getCreatedAt());
        orderDO.setPaidAt(order.getPaidAt());
        orderDO.setCancelledAt(order.getCancelledAt());
        return orderDO;
    }

    public static List<OrderItemDO> toOrderItemDOs(Order order) {
        List<OrderItemDO> itemDOs = new ArrayList<OrderItemDO>();
        for (OrderItem orderItem : order.itemList()) {
            OrderItemDO itemDO = new OrderItemDO();
            itemDO.setOrderItemId(orderItem.getOrderItemId());
            itemDO.setOrderNo(orderItem.getOrderNo());
            itemDO.setSkuId(orderItem.getSkuId());
            itemDO.setSkuName(orderItem.getSkuName());
            itemDO.setQuantity(orderItem.getQuantity());
            itemDO.setUnitPrice(orderItem.getUnitPrice());
            itemDO.setLineAmount(orderItem.getLineAmount());
            itemDOs.add(itemDO);
        }
        return itemDOs;
    }

    public static Order toOrder(OrderDO orderDO, List<OrderItemDO> itemDOs) {
        Order order = new Order();
        order.setOrderId(orderDO.getOrderId());
        order.setOrderNo(orderDO.getOrderNo());
        order.setUserId(orderDO.getUserId());
        order.setStoreId(orderDO.getStoreId());
        order.setStatus(resolveStatus(orderDO.getStatus()));
        order.setTotalAmount(orderDO.getTotalAmount());
        order.setCreatedAt(orderDO.getCreatedAt());
        order.setPaidAt(orderDO.getPaidAt());
        order.setCancelledAt(orderDO.getCancelledAt());

        if (itemDOs != null) {
            for (OrderItemDO itemDO : itemDOs) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(itemDO.getOrderItemId());
                item.setOrderNo(itemDO.getOrderNo());
                item.setSkuId(itemDO.getSkuId());
                item.setSkuName(itemDO.getSkuName());
                item.setQuantity(itemDO.getQuantity());
                item.setUnitPrice(itemDO.getUnitPrice());
                item.setLineAmount(itemDO.getLineAmount());
                order.getOrderItems().add(item);
            }
        }
        return order;
    }

    private static OrderStatus resolveStatus(Integer statusValue) {
        if (statusValue == null) {
            return null;
        }
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getValue().equals(statusValue)) {
                return status;
            }
        }
        return null;
    }
}
