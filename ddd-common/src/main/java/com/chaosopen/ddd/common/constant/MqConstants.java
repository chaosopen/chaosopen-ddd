package com.chaosopen.ddd.common.constant;

/**
 * MQ 常量定义。
 */
public final class MqConstants {

    public static final String ORDER_SMS_EXCHANGE = "order.sms.exchange";
    public static final String ORDER_SMS_QUEUE = "order.sms.queue";
    public static final String ORDER_SMS_ROUTING_KEY = "order.sms.created";

    private MqConstants() {
    }
}
