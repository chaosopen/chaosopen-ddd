package com.chaosopen.ddd.infrastructure.inventory.gatewayImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaosopen.ddd.domain.inventory.gateway.InventoryGateway;
import com.chaosopen.ddd.domain.inventory.model.Inventory;
import com.chaosopen.ddd.infrastructure.inventory.persistence.mapper.InventoryMapper;
import com.chaosopen.ddd.infrastructure.inventory.persistence.dataobject.InventoryDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class InventoryGatewayImpl implements InventoryGateway {

    @Resource
    private InventoryMapper inventoryMapper;

    @Override
    public Optional<Inventory> findByStoreIdAndSkuId(Long storeId, Long skuId) {
        LambdaQueryWrapper<InventoryDO> queryWrapper = new LambdaQueryWrapper<InventoryDO>()
                .eq(InventoryDO::getStoreId, storeId)
                .eq(InventoryDO::getSkuId, skuId)
                .last("limit 1");
        InventoryDO inventoryDO = inventoryMapper.selectOne(queryWrapper);
        if (inventoryDO == null) {
            return Optional.empty();
        }

        Inventory inventory = new Inventory();
        inventory.setInventoryId(inventoryDO.getInventoryId());
        inventory.setStoreId(inventoryDO.getStoreId());
        inventory.setSkuId(inventoryDO.getSkuId());
        inventory.setTotalStock(inventoryDO.getTotalStock());
        inventory.setLockedStock(inventoryDO.getLockedStock());
        inventory.setAvailableStock(inventoryDO.getAvailableStock());
        return Optional.of(inventory);
    }

    @Override
    public void save(Inventory inventory) {
        InventoryDO inventoryDO = new InventoryDO();
        inventoryDO.setInventoryId(inventory.getInventoryId());
        inventoryDO.setStoreId(inventory.getStoreId());
        inventoryDO.setSkuId(inventory.getSkuId());
        inventoryDO.setTotalStock(inventory.getTotalStock());
        inventoryDO.setLockedStock(inventory.getLockedStock());
        inventoryDO.setAvailableStock(inventory.getAvailableStock());

        if (inventoryDO.getInventoryId() == null) {
            inventoryMapper.insert(inventoryDO);
            inventory.setInventoryId(inventoryDO.getInventoryId());
        } else {
            inventoryMapper.updateById(inventoryDO);
        }
    }

    @Override
    public void saveBatch(List<Inventory> inventories) {
        if (inventories == null || inventories.isEmpty()) {
            return;
        }
        for (Inventory inventory : inventories) {
            save(inventory);
        }
    }
}
