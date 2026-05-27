package com.chaosopen.ddd.infrastructure.order.gatewayImpl;

import com.chaosopen.ddd.client.dto.mq.OrderCreatedMessage;
import com.chaosopen.ddd.common.constant.MqConstants;
import com.chaosopen.ddd.domain.order.gateway.OrderMessageGateway;
import com.chaosopen.ddd.domain.order.model.OrderCreatedMessageModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 订单消息网关实现。
 */
@Component
public class OrderMessageGatewayImpl implements OrderMessageGateway {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void publishOrderCreated(OrderCreatedMessageModel message) {
        if (message == null) {
            return;
        }
        OrderCreatedMessage mqMessage = new OrderCreatedMessage(
                message.getOrderId(),
                message.getOrderNo(),
                message.getUserId(),
                message.getMobile(),
                message.getTotalAmount(),
                message.getStoreId(),
                message.getTotalQuantity()
        );
        rabbitTemplate.convertAndSend(MqConstants.ORDER_CREATED_FANOUT_EXCHANGE, "", mqMessage);
    }
}
