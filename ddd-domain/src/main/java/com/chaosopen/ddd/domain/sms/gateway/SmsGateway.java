package com.chaosopen.ddd.domain.sms.gateway;

import com.chaosopen.ddd.domain.sms.model.SmsModel;

public interface SmsGateway {

    /**
     * 发布短信消息到MQ（异步）。
     */
    void publishSms(SmsModel message);

    /**
     * 执行供应商短信发送（消费端调用）。
     */
    void sendSms(SmsModel message);
}
