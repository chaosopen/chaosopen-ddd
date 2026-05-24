package com.chaosopen.ddd.domain.inventory.service;

import com.chaosopen.ddd.domain.inventory.model.Inventory;
import com.chaosopen.ddd.domain.order.model.OrderItem;
import com.chaosopen.ddd.domain.store.model.Store;

import java.util.List;

/**
 * 库存领域服务：负责库存规则与库存变更持久化。
 */
public interface InventoryDomainService {

    /**
     * 按订单项批量扣减门店库存并持久化。
     *
     * @param store 门店实体
     * @param items 订单项
     * @return 扣减后的库存实体列表
     */
    List<Inventory> deductAndSaveBatch(Store store, List<OrderItem> items);
}
