package com.chaosopen.ddd.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单创建外发消息领域中转模型。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedMessageModel {

    private Long orderId;
    private String orderNo;
    private Long userId;
    private String mobile;
    private BigDecimal totalAmount;
    private Long storeId;
    private Integer totalQuantity;
}
