package com.chaosopen.ddd.application.converter.product;

import com.chaosopen.ddd.client.dto.clientobject.ProductInfoCO;
import com.chaosopen.ddd.domain.product.model.Product;
import com.chaosopen.ddd.domain.product.model.Sku;

import java.util.ArrayList;
import java.util.List;

public final class ProductApplicationConverter {

    private ProductApplicationConverter() {
    }

    public static ProductInfoCO toProductInfoCO(Product product) {
        List<ProductInfoCO.SkuCO> skuList = new ArrayList<ProductInfoCO.SkuCO>();
        for (Sku sku : product.skuList()) {
            skuList.add(new ProductInfoCO.SkuCO(
                    sku.getSkuId(),
                    sku.getSkuCode(),
                    sku.getSkuName(),
                    sku.getSalePrice(),
                    sku.getShelfStatus() == null ? null : sku.getShelfStatus().getValue(),
                    sku.getShelfStatus() == null ? null : sku.getShelfStatus().getDesc()
            ));
        }

        return new ProductInfoCO(
                product.getProductId(),
                product.getProductName(),
                product.getShelfStatus() == null ? null : product.getShelfStatus().getValue(),
                product.getShelfStatus() == null ? null : product.getShelfStatus().getDesc(),
                product.getSalesCount(),
                skuList
        );
    }
}
