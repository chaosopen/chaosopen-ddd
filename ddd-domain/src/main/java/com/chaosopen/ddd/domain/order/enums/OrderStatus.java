package com.chaosopen.ddd.domain.order.enums;

import lombok.Getter;

/**
 * 订单状态枚举。
 */
@Getter
public enum OrderStatus {

    /**
     * 初始化。
     */
    INIT(0, "初始化"),
    /**
     * 待支付。
     */
    PENDING_PAYMENT(10, "待支付"),
    /**
     * 已支付。
     */
    PAID(20, "已支付"),
    /**
     * 已发货。
     */
    SHIPPED(30, "已发货"),
    /**
     * 已取消。
     */
    CANCELLED(40, "已取消"),
    /**
     * 已关闭。
     */
    CLOSED(50, "已关闭");

    /**
     * 状态值。
     */
    private final Integer value;
    /**
     * 状态说明。
     */
    private final String desc;

    /**
     * 构造函数。
     *
     * @param value 状态值
     * @param desc 状态说明
     */
    OrderStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
