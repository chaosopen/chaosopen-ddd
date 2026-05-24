package com.chaosopen.ddd.infrastructure.product.gatewayImpl;

import com.chaosopen.ddd.domain.product.gateway.SkuGateway;
import com.chaosopen.ddd.domain.product.enums.ProductShelfStatus;
import com.chaosopen.ddd.domain.product.model.Sku;
import com.chaosopen.ddd.infrastructure.product.persistence.mapper.SkuMapper;
import com.chaosopen.ddd.infrastructure.product.persistence.dataobject.SkuDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class SkuGatewayImpl implements SkuGateway {

    @Resource
    private SkuMapper skuMapper;

    @Override
    public Optional<Sku> findBySkuId(Long skuId) {
        SkuDO skuDO = skuMapper.selectById(skuId);
        if (skuDO == null) {
            return Optional.empty();
        }
        Sku sku = new Sku();
        sku.setSkuId(skuDO.getSkuId());
        sku.setProductId(skuDO.getProductId());
        sku.setSkuCode(skuDO.getSkuCode());
        sku.setSkuName(skuDO.getSkuName());
        sku.setSalePrice(skuDO.getSalePrice());
        sku.setShelfStatus(resolveShelfStatus(skuDO.getShelfStatus()));
        return Optional.of(sku);
    }

    private ProductShelfStatus resolveShelfStatus(Integer value) {
        if (value == null) {
            return null;
        }
        return value == ProductShelfStatus.ON_SHELF.getValue() ? ProductShelfStatus.ON_SHELF : ProductShelfStatus.OFF_SHELF;
    }
}
