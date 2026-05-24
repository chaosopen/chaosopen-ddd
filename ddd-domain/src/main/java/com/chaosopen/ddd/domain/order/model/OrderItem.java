package com.chaosopen.ddd.domain.order.model;

import com.chaosopen.ddd.common.enums.ErrorCode;
import com.chaosopen.ddd.common.exception.BizException;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单项实体。
 */
@Data
public class OrderItem {

    /**
     * 订单项ID。
     */
    private Long orderItemId;
    /**
     * 订单号。
     */
    private String orderNo;
    /**
     * SKU ID。
     */
    private Long skuId;
    /**
     * SKU名称。
     */
    private String skuName;
    /**
     * 购买数量。
     */
    private Integer quantity;
    /**
     * 商品单价。
     */
    private BigDecimal unitPrice;
    /**
     * 行项目金额。
     */
    private BigDecimal lineAmount;

    public static OrderItem of(Long skuId, String skuName, Integer quantity, BigDecimal unitPrice) {
        OrderItem item = new OrderItem();
        item.setSkuId(skuId);
        item.setSkuName(skuName);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        item.validate();
        item.recalculateLineAmount();
        return item;
    }

    public void validate() {
        if (skuId == null) {
            throw new BizException(ErrorCode.FAILED, "skuId不能为空");
        }
        if (quantity == null || quantity <= 0) {
            throw new BizException(ErrorCode.FAILED, "购买数量必须大于0");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BizException(ErrorCode.FAILED, "商品单价不能小于0");
        }
    }

    public void recalculateLineAmount() {
        validate();
        this.lineAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
