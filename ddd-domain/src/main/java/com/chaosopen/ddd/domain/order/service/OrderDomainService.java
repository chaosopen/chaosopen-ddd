package com.chaosopen.ddd.domain.order.service;

import com.chaosopen.ddd.domain.order.model.OrderItem;
import com.chaosopen.ddd.domain.order.dto.CreateOrderResult;

import java.util.List;

/**
 * 订单领域服务，承载跨聚合的领域行为编排。
 */
public interface OrderDomainService {

    /**
     * 根据业务规则完成下单。
     *
     * @param userId 用户ID
     * @param storeId 门店ID
     * @param items 订单项
     * @return 下单结果，包含订单与本次规则执行产生的变更对象
     */
    CreateOrderResult placeOrder(Long userId, Long storeId, List<OrderItem> items);
}
