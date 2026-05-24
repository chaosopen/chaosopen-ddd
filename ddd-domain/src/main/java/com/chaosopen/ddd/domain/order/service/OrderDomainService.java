package com.chaosopen.ddd.domain.order.service;

import com.chaosopen.ddd.domain.order.model.OrderItem;
import com.chaosopen.ddd.domain.order.dto.CreateOrderResult;
import com.chaosopen.ddd.domain.product.model.Sku;
import com.chaosopen.ddd.domain.store.model.Store;
import com.chaosopen.ddd.domain.user.model.User;

import java.util.List;
import java.util.Map;

/**
 * 订单领域服务，承载订单领域行为。
 */
public interface OrderDomainService {

    /**
     * 根据业务规则完成下单。
     *
     * @param items 订单项
     * @param user 用户实体（由应用层准备）
     * @param store 门店实体（由应用层准备）
     * @param skuMap sku实体映射（由应用层准备）
     * @return 下单结果
     */
    CreateOrderResult placeOrder(List<OrderItem> items, User user, Store store, Map<Long, Sku> skuMap);
}
