package com.chaosopen.ddd.domain.order.service.impl;

import com.chaosopen.ddd.common.enums.ErrorCode;
import com.chaosopen.ddd.common.exception.BizException;
import com.chaosopen.ddd.domain.event.DomainEventPublisher;
import com.chaosopen.ddd.domain.order.dto.CreateOrderResult;
import com.chaosopen.ddd.domain.order.event.OrderCreatedDomainEvent;
import com.chaosopen.ddd.domain.order.gateway.OrderGateway;
import com.chaosopen.ddd.domain.order.model.OrderCreateInfo;
import com.chaosopen.ddd.domain.order.model.Order;
import com.chaosopen.ddd.domain.order.model.OrderItem;
import com.chaosopen.ddd.domain.order.service.OrderDomainService;
import com.chaosopen.ddd.domain.product.model.Sku;
import com.chaosopen.ddd.domain.store.model.Store;
import com.chaosopen.ddd.domain.user.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单领域服务默认实现。
 * 说明：只负责订单领域行为，不跨领域做商品/库存/用户操作。
 */
public class OrderDomainServiceImpl implements OrderDomainService {

    private OrderGateway orderGateway;
    private DomainEventPublisher domainEventPublisher;

    public void setOrderGateway(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public void setDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public CreateOrderResult placeOrder(List<OrderItem> items, User user, Store store, Map<Long, Sku> skuMap) {
        validateOrderItems(items);
        OrderCreateInfo orderCreateInfo = buildOrderCreateInfo(items, user, store, skuMap);
        Order order = buildAndSubmitOrder(orderCreateInfo);
        int totalQuantity = order.totalQuantity();
        orderGateway.save(order);
        publishOrderCreatedEvent(order, orderCreateInfo.getUser().getMobile());
        return new CreateOrderResult(order, totalQuantity);
    }

    private void validateOrderItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new BizException(ErrorCode.FAILED, "创建订单时订单项不能为空");
        }
    }

    private OrderCreateInfo buildOrderCreateInfo(List<OrderItem> items,
                                                 User user,
                                                 Store store,
                                                 Map<Long, Sku> skuMap) {
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        if (store == null || store.getStoreId() == null) {
            throw new BizException(ErrorCode.FAILED, "门店信息不能为空");
        }
        if (skuMap == null || skuMap.isEmpty()) {
            throw new BizException(ErrorCode.SKU_NOT_FOUND, "SKU不存在");
        }
        List<OrderItem> trustedItems = new ArrayList<OrderItem>();
        for (OrderItem item : items) {
            Sku sku = skuMap.get(item.getSkuId());
            if (sku == null) {
                throw new BizException(ErrorCode.SKU_NOT_FOUND, "SKU不存在");
            }
            BigDecimal unitPrice = sku.getSalePrice() == null ? BigDecimal.ZERO : sku.getSalePrice();
            trustedItems.add(OrderItem.of(sku.getSkuId(), sku.getSkuName(), item.getQuantity(), unitPrice));
        }
        return new OrderCreateInfo(user, store, trustedItems);
    }

    private Order buildAndSubmitOrder(OrderCreateInfo orderCreateInfo) {
        Order order = Order.init(orderCreateInfo.getUser().getUserId(), orderCreateInfo.getStore().getStoreId());
        for (OrderItem item : orderCreateInfo.getOrderItems()) {
            order.addItem(item);
        }
        order.submit();
        return order;
    }

    private void publishOrderCreatedEvent(Order order, String mobile) {
        OrderCreatedDomainEvent event = new OrderCreatedDomainEvent();
        event.setOrderId(order.getOrderId());
        event.setOrderNo(order.getOrderNo());
        event.setUserId(order.getUserId());
        event.setMobile(mobile);
        event.setTotalAmount(order.getTotalAmount());
        event.setStoreId(order.getStoreId());
        event.setCreateTime(LocalDateTime.now());
        domainEventPublisher.publish(event);
    }
}
