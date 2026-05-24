package com.chaosopen.ddd.adapter.mq.consumer;

import com.chaosopen.ddd.client.dto.mq.OrderCreatedMessage;
import com.chaosopen.ddd.common.constant.MqConstants;
import com.chaosopen.ddd.domain.sms.gateway.SmsGateway;
import com.chaosopen.ddd.domain.sms.model.SmsModel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单短信消息消费者（Adapter 入站监听）。
 */
@Component
public class OrderCreatedSmsConsumer {

    @Autowired
    private SmsGateway smsGateway;

    @RabbitListener(queues = MqConstants.ORDER_SMS_QUEUE)
    public void onMessage(OrderCreatedMessage message) {
        if (message == null || message.getMobile() == null || message.getMobile().trim().isEmpty()) {
            return;
        }
        Map<String, String> templateParams = new HashMap<String, String>();
        templateParams.put("orderNo", message.getOrderNo());
        templateParams.put("totalAmount", String.valueOf(message.getTotalAmount()));

        SmsModel domainMessage = new SmsModel(
                message.getMobile(),
                "ORDER_CREATED_NOTICE",
                templateParams
        );
        smsGateway.sendSms(domainMessage);
    }
}
