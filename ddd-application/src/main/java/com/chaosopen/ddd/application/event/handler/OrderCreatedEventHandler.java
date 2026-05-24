package com.chaosopen.ddd.application.event.handler;

import com.chaosopen.ddd.domain.order.event.OrderCreatedDomainEvent;
import com.chaosopen.ddd.domain.sms.gateway.SmsGateway;
import com.chaosopen.ddd.domain.sms.model.SmsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建订单事件处理器：将事件转换为短信MQ消息。
 */
@Component
public class OrderCreatedEventHandler {

    @Autowired
    private SmsGateway smsGateway;

    @EventListener
    public void handle(OrderCreatedDomainEvent event) {
        if (event == null || event.getMobile() == null || event.getMobile().trim().isEmpty()) {
            return;
        }
        Map<String, String> templateParams = new HashMap<String, String>();
        templateParams.put("orderNo", event.getOrderNo());
        templateParams.put("totalAmount", String.valueOf(event.getTotalAmount()));

        SmsModel smsMessage = new SmsModel(
                event.getMobile(),
                "ORDER_CREATED_NOTICE",
                templateParams
        );
        smsGateway.publishSms(smsMessage);
    }
}
