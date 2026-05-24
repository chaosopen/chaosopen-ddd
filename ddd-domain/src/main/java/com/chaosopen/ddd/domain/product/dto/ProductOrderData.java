package com.chaosopen.ddd.domain.product.dto;

import com.chaosopen.ddd.domain.product.model.Sku;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * 下单阶段商品域返回的数据。
 */
@Data
@AllArgsConstructor
public class ProductOrderData {

    /**
     * key=productId, value=销量增量。
     */
    private Map<Long, Integer> productSalesDelta;
    /**
     * key=skuId, value=sku实体。
     */
    private Map<Long, Sku> skuMap;
}
