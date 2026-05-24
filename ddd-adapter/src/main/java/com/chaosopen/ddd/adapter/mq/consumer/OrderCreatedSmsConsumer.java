package com.chaosopen.ddd.adapter.mq.consumer;

import com.chaosopen.ddd.client.dto.mq.SmsMessage;
import com.chaosopen.ddd.common.constant.MqConstants;
import com.chaosopen.ddd.domain.sms.gateway.SmsGateway;
import com.chaosopen.ddd.domain.sms.model.SmsModel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单短信消息消费者（Adapter 入站监听）。
 */
@Component
public class OrderCreatedSmsConsumer {

    @Autowired
    private SmsGateway smsGateway;

    @RabbitListener(queues = MqConstants.ORDER_SMS_QUEUE)
    public void onMessage(SmsMessage smsMessage) {
        if (smsMessage == null) {
            return;
        }
        SmsModel domainMessage = new SmsModel(
                smsMessage.getMobile(),
                smsMessage.getTemplateCode(),
                smsMessage.getTemplateParams()
        );
        smsGateway.sendSms(domainMessage);
    }
}
