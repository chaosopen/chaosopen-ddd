package com.chaosopen.ddd.infrastructure.sms.gatewayImpl;

import com.chaosopen.ddd.client.dto.mq.SmsMessage;
import com.chaosopen.ddd.common.constant.MqConstants;
import com.chaosopen.ddd.domain.sms.gateway.SmsGateway;
import com.chaosopen.ddd.domain.sms.model.SmsModel;
import com.chaosopen.ddd.infrastructure.sms.remote.SmsRemoteClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SmsGatewayImpl implements SmsGateway {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private SmsRemoteClient smsRemoteClient;

    @Override
    public void publishSms(SmsModel message) {
        if (message == null) {
            return;
        }
        SmsMessage mqMessage = new SmsMessage(
                message.getMobile(),
                message.getTemplateCode(),
                message.getTemplateParams()
        );
        rabbitTemplate.convertAndSend(
                MqConstants.ORDER_SMS_EXCHANGE,
                MqConstants.ORDER_SMS_ROUTING_KEY,
                mqMessage
        );
    }

    @Override
    public void sendSms(SmsModel message) {
        if (message == null) {
            return;
        }
        smsRemoteClient.sendByTemplate(
                message.getMobile(),
                message.getTemplateCode(),
                message.getTemplateParams()
        );
    }
}
