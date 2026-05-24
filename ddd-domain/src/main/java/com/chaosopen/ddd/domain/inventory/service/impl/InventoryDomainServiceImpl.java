package com.chaosopen.ddd.domain.inventory.service.impl;

import com.chaosopen.ddd.common.enums.ErrorCode;
import com.chaosopen.ddd.common.exception.BizException;
import com.chaosopen.ddd.domain.inventory.gateway.InventoryGateway;
import com.chaosopen.ddd.domain.inventory.model.Inventory;
import com.chaosopen.ddd.domain.inventory.service.InventoryDomainService;
import com.chaosopen.ddd.domain.order.model.OrderItem;
import com.chaosopen.ddd.domain.store.model.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存领域服务实现。
 */
public class InventoryDomainServiceImpl implements InventoryDomainService {

    private InventoryGateway inventoryGateway;

    public void setInventoryGateway(InventoryGateway inventoryGateway) {
        this.inventoryGateway = inventoryGateway;
    }

    @Override
    public List<Inventory> deductAndSaveBatch(Store store, List<OrderItem> items) {
        if (store == null || store.getStoreId() == null) {
            throw new BizException(ErrorCode.FAILED, "门店信息不能为空");
        }
        List<Inventory> updatedInventories = new ArrayList<Inventory>();
        for (OrderItem item : items) {
            Inventory inventory = inventoryGateway.findByStoreIdAndSkuId(store.getStoreId(), item.getSkuId())
                    .orElseThrow(() -> new BizException(ErrorCode.INVENTORY_OVER_SOLD, "门店商品库存不存在"));
            if (inventory.getAvailableStock() == null || inventory.getAvailableStock() <= 0) {
                throw new BizException(ErrorCode.INVENTORY_OVER_SOLD, "门店商品库存为0，不能下单");
            }
            if (!inventory.lock(item.getQuantity())) {
                throw new BizException(ErrorCode.INVENTORY_LOCK_FAILED, "库存不足，扣减失败");
            }
            updatedInventories.add(inventory);
        }
        inventoryGateway.saveBatch(updatedInventories);
        return updatedInventories;
    }
}
