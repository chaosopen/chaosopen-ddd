package com.chaosopen.ddd.adapter.mq.consumer;

import com.chaosopen.ddd.application.command.order.SendOrderCreatedSmsCmdExe;
import com.chaosopen.ddd.client.dto.mq.OrderCreatedMessage;
import com.chaosopen.ddd.common.constant.MqConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单短信消息消费者（Adapter 入站监听）。
 */
@Component
public class OrderCreatedSmsConsumer {

    @Autowired
    private SendOrderCreatedSmsCmdExe sendOrderCreatedSmsCmdExe;

    @RabbitListener(queues = MqConstants.ORDER_SMS_QUEUE)
    public void onMessage(OrderCreatedMessage message) {
        sendOrderCreatedSmsCmdExe.execute(message);
    }
}
