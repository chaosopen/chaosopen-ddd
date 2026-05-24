package com.chaosopen.ddd.domain.product.model;

import com.chaosopen.ddd.domain.product.enums.ProductShelfStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 商品聚合根。
 */
@Data
public class Product {

    /**
     * 商品ID。
     */
    private Long productId;
    /**
     * 商品名称。
     */
    private String productName;
    /**
     * 商品上下架状态。
     */
    private ProductShelfStatus shelfStatus;
    /**
     * 销量。
     */
    private Integer salesCount;
    /**
     * 创建时间。
     */
    private LocalDateTime createdAt;
    /**
     * 更新时间。
     */
    private LocalDateTime updatedAt;
    /**
     * SKU列表。
     */
    private List<Sku> skus = new ArrayList<Sku>();

    /**
     * 向商品添加SKU。
     *
     * @param sku SKU实体
     */
    public void addSku(Sku sku) {
        if (sku != null) {
            skus.add(sku);
        }
    }

    /**
     * 获取只读SKU列表。
     *
     * @return SKU列表
     */
    public List<Sku> skuList() {
        return Collections.unmodifiableList(skus);
    }

    /**
     * 商品上架。
     */
    public void onShelf() {
        this.shelfStatus = ProductShelfStatus.ON_SHELF;
    }

    /**
     * 商品下架。
     */
    public void offShelf() {
        this.shelfStatus = ProductShelfStatus.OFF_SHELF;
    }

    /**
     * 增加销量。
     *
     * @param quantity 增加数量
     */
    public void increaseSales(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return;
        }
        int current = salesCount == null ? 0 : salesCount;
        salesCount = current + quantity;
    }
}
