package com.chaosopen.ddd.domain.product.service.impl;

import com.chaosopen.ddd.common.enums.ErrorCode;
import com.chaosopen.ddd.common.exception.BizException;
import com.chaosopen.ddd.domain.order.model.OrderItem;
import com.chaosopen.ddd.domain.product.dto.ProductOrderData;
import com.chaosopen.ddd.domain.product.gateway.ProductGateway;
import com.chaosopen.ddd.domain.product.gateway.SkuGateway;
import com.chaosopen.ddd.domain.product.model.Product;
import com.chaosopen.ddd.domain.product.model.Sku;
import com.chaosopen.ddd.domain.product.service.ProductDomainService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品领域服务实现。
 */
public class ProductDomainServiceImpl implements ProductDomainService {

    private SkuGateway skuGateway;
    private ProductGateway productGateway;

    public void setSkuGateway(SkuGateway skuGateway) {
        this.skuGateway = skuGateway;
    }

    public void setProductGateway(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    public ProductOrderData validateSkusAndCollectOrderData(List<OrderItem> items) {
        Map<Long, Integer> salesDelta = new HashMap<Long, Integer>();
        Map<Long, Sku> skuMap = new HashMap<Long, Sku>();
        for (OrderItem item : items) {
            Sku sku = skuGateway.findBySkuId(item.getSkuId())
                    .orElseThrow(() -> new BizException(ErrorCode.SKU_NOT_FOUND, "SKU不存在"));
            if (!sku.isOnShelf()) {
                throw new BizException(ErrorCode.PRODUCT_OFF_SHELF, "商品未上架，不能下单");
            }
            skuMap.put(sku.getSkuId(), sku);
            Integer current = salesDelta.containsKey(sku.getProductId()) ? salesDelta.get(sku.getProductId()) : 0;
            salesDelta.put(sku.getProductId(), current + item.getQuantity());
        }
        return new ProductOrderData(salesDelta, skuMap);
    }

    @Override
    public void increaseSalesAndSave(Map<Long, Integer> productSalesDelta) {
        for (Map.Entry<Long, Integer> entry : productSalesDelta.entrySet()) {
            Product product = productGateway.findByProductId(entry.getKey())
                    .orElseThrow(() -> new BizException(ErrorCode.PRODUCT_NOT_FOUND, "商品不存在"));
            product.increaseSales(entry.getValue());
            productGateway.save(product);
        }
    }
}
