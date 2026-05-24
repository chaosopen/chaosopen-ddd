package com.chaosopen.ddd.domain.inventory.gateway;

import com.chaosopen.ddd.domain.inventory.model.Inventory;

import java.util.Optional;

public interface InventoryGateway {

    Optional<Inventory> findByStoreIdAndSkuId(Long storeId, Long skuId);

    void save(Inventory inventory);
}
