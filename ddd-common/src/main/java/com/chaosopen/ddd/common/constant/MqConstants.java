package com.chaosopen.ddd.common.constant;

/**
 * MQ 常量定义。
 */
public final class MqConstants {

    public static final String ORDER_SMS_EXCHANGE = "order.sms.exchange";
    public static final String ORDER_SMS_ROUTING_KEY = "order.sms.created";
    public static final String ORDER_CREATED_FANOUT_EXCHANGE = "order.created.fanout.exchange";
    public static final String ORDER_SMS_QUEUE = "order.sms.queue";
    public static final String USER_POINTS_QUEUE = "user.points.queue";

    private MqConstants() {
    }
}
