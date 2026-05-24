package com.chaosopen.ddd.application.event.handler;

import com.chaosopen.ddd.client.dto.mq.OrderCreatedMessage;
import com.chaosopen.ddd.domain.order.event.OrderCreatedDomainEvent;
import com.chaosopen.ddd.domain.order.gateway.OrderMessageGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 创建订单事件处理器：发布下单广播消息。
 */
@Component
public class OrderCreatedEventHandler {

    @Autowired
    private OrderMessageGateway orderMessageGateway;

    @EventListener
    public void handle(OrderCreatedDomainEvent event) {
        if (event == null) {
            return;
        }
        OrderCreatedMessage message = new OrderCreatedMessage(
                event.getOrderId(),
                event.getOrderNo(),
                event.getUserId(),
                event.getMobile(),
                event.getTotalAmount(),
                event.getStoreId(),
                event.getTotalQuantity()
        );
        orderMessageGateway.publishOrderCreated(message);
    }
}
