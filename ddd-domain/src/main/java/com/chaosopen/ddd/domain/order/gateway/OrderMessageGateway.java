package com.chaosopen.ddd.domain.order.gateway;

import com.chaosopen.ddd.domain.order.model.OrderCreatedMessageModel;

/**
 * 订单消息网关抽象。
 */
public interface OrderMessageGateway {

    /**
     * 发布下单成功广播消息。
     *
     * @param message 消息体
     */
    void publishOrderCreated(OrderCreatedMessageModel message);
}
