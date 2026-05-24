package com.chaosopen.ddd.client.dto.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 下单成功广播消息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private String orderNo;
    private Long userId;
    private String mobile;
    private BigDecimal totalAmount;
    private Long storeId;
    private Integer totalQuantity;
}
