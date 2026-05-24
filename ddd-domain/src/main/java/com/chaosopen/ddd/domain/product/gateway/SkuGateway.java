package com.chaosopen.ddd.domain.product.gateway;

import com.chaosopen.ddd.domain.product.model.Sku;

import java.util.Optional;

public interface SkuGateway {

    Optional<Sku> findBySkuId(Long skuId);
}
