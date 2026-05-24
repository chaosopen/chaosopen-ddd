package com.chaosopen.ddd.domain.product.service;

import com.chaosopen.ddd.domain.product.dto.ProductOrderData;
import com.chaosopen.ddd.domain.order.model.OrderItem;

import java.util.List;
import java.util.Map;

/**
 * 商品领域服务：负责商品域规则校验与状态变更。
 */
public interface ProductDomainService {

    /**
     * 校验订单项中的SKU是否可下单，并汇总每个商品的销量增量。
     *
     * @param items 订单项
     * @return key=productId, value=销量增量
     */
    ProductOrderData validateSkusAndCollectOrderData(List<OrderItem> items);

    /**
     * 根据销量增量更新商品销量并持久化。
     *
     * @param productSalesDelta 商品销量增量
     */
    void increaseSalesAndSave(Map<Long, Integer> productSalesDelta);
}
