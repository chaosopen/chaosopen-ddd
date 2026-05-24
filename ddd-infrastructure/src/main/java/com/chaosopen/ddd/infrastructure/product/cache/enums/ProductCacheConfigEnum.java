package com.chaosopen.ddd.infrastructure.product.cache.enums;

import java.time.Duration;

public enum ProductCacheConfigEnum {

    PRODUCT_INFO("ddd:product:info:", Duration.ofMinutes(30));

    private final String keyPrefix;
    private final Duration ttl;

    ProductCacheConfigEnum(String keyPrefix, Duration ttl) {
        this.keyPrefix = keyPrefix;
        this.ttl = ttl;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public Duration getTtl() {
        return ttl;
    }
}
