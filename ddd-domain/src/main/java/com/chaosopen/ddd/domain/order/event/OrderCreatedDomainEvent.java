package com.chaosopen.ddd.domain.order.event;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单创建领域事件。
 */
@Data
public class OrderCreatedDomainEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID。
     */
    private Long orderId;
    /**
     * 订单号。
     */
    private String orderNo;
    /**
     * 用户ID。
     */
    private Long userId;
    /**
     * 用户手机号。
     */
    private String mobile;
    /**
     * 订单总金额。
     */
    private BigDecimal totalAmount;
    /**
     * 门店ID。
     */
    private Long storeId;
    /**
     * 事件创建时间。
     */
    private LocalDateTime createTime;
}
