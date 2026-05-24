package com.chaosopen.ddd.domain.order.dto;

import com.chaosopen.ddd.domain.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 下单结果。
 */
@Data
@AllArgsConstructor
public class CreateOrderResult {

    private Order order;
    private Integer orderTotalQuantity;
}
