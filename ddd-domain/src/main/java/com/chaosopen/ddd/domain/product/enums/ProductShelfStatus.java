package com.chaosopen.ddd.domain.product.enums;

import lombok.Getter;

/**
 * 商品上下架状态枚举。
 */
@Getter
public enum ProductShelfStatus {

    /**
     * 已下架。
     */
    OFF_SHELF(0, "已下架"),
    /**
     * 已上架。
     */
    ON_SHELF(1, "已上架");

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
    ProductShelfStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
