package com.chaosopen.ddd.domain.inventory.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门店SKU库存聚合根。
 */
@Data
public class Inventory {

    /**
     * 库存ID。
     */
    private Long inventoryId;
    /**
     * 门店ID。
     */
    private Long storeId;
    /**
     * SKU ID。
     */
    private Long skuId;
    /**
     * 总库存。
     */
    private Integer totalStock;
    /**
     * 锁定库存。
     */
    private Integer lockedStock;
    /**
     * 可用库存。
     */
    private Integer availableStock;
    /**
     * 创建时间。
     */
    private LocalDateTime createdAt;
    /**
     * 更新时间。
     */
    private LocalDateTime updatedAt;

    /**
     * 判断是否可锁定库存。
     *
     * @param quantity 锁定数量
     * @return true表示可锁定
     */
    public boolean canLock(Integer quantity) {
        return quantity != null && quantity > 0 && availableStock != null && availableStock >= quantity;
    }

    /**
     * 锁定库存。
     *
     * @param quantity 锁定数量
     * @return true表示锁定成功
     */
    public boolean lock(Integer quantity) {
        if (!canLock(quantity)) {
            return false;
        }
        availableStock = availableStock - quantity;
        lockedStock = (lockedStock == null ? 0 : lockedStock) + quantity;
        return true;
    }

    /**
     * 释放库存。
     *
     * @param quantity 释放数量
     */
    public void release(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return;
        }
        int locked = lockedStock == null ? 0 : lockedStock;
        int releaseQty = Math.min(locked, quantity);
        lockedStock = locked - releaseQty;
        availableStock = (availableStock == null ? 0 : availableStock) + releaseQty;
    }
}
