package com.chaosopen.ddd.domain.order.gateway;

import com.chaosopen.ddd.client.dto.mq.OrderCreatedMessage;

/**
 * 订单消息网关抽象。
 */
public interface OrderMessageGateway {

    /**
     * 发布下单成功广播消息。
     *
     * @param message 消息体
     */
    void publishOrderCreated(OrderCreatedMessage message);
}
