package com.chaosopen.ddd.application.command.order;

import com.chaosopen.ddd.client.dto.mq.OrderCreatedMessage;
import com.chaosopen.ddd.domain.sms.gateway.SmsGateway;
import com.chaosopen.ddd.domain.sms.model.SmsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 下单短信发送执行器（应用层编排）。
 */
@Component
public class SendOrderCreatedSmsCmdExe {

    @Autowired
    private SmsGateway smsGateway;

    public void execute(OrderCreatedMessage message) {
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
