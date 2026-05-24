package com.chaosopen.ddd.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS("0000", "成功"),
    FAILED("9999", "失败"),
    ORDER_NOT_FOUND("1001", "订单不存在"),
    ORDER_STATUS_INVALID("1002", "订单状态非法"),
    ORDER_ALREADY_PAID("1003", "订单已支付"),
    PRODUCT_OFF_SHELF("2001", "商品已下架"),
    SKU_NOT_FOUND("2002", "SKU不存在"),
    PRODUCT_NOT_FOUND("2003", "商品不存在"),
    USER_NOT_FOUND("2004", "用户不存在"),
    INVENTORY_OVER_SOLD("3001", "库存超卖"),
    INVENTORY_LOCK_FAILED("3002", "库存锁定失败");

    private final String value;
    private final String desc;

    ErrorCode(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
