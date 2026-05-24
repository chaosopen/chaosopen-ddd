package com.chaosopen.ddd.domain.product.model;

import com.chaosopen.ddd.domain.product.enums.ProductShelfStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品SKU实体。
 */
@Data
public class Sku {

    /**
     * SKU ID。
     */
    private Long skuId;
    /**
     * 商品ID。
     */
    private Long productId;
    /**
     * SKU编码。
     */
    private String skuCode;
    /**
     * SKU名称。
     */
    private String skuName;
    /**
     * 销售价。
     */
    private BigDecimal salePrice;
    /**
     * 上下架状态。
     */
    private ProductShelfStatus shelfStatus;
    /**
     * 创建时间。
     */
    private LocalDateTime createdAt;
    /**
     * 更新时间。
     */
    private LocalDateTime updatedAt;

    /**
     * 判断SKU是否上架。
     *
     * @return true表示已上架
     */
    public boolean isOnShelf() {
        return ProductShelfStatus.ON_SHELF == shelfStatus;
    }
}
