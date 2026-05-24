package com.chaosopen.ddd.infrastructure.product.cache;

import com.chaosopen.ddd.domain.product.model.Product;
import com.chaosopen.ddd.infrastructure.product.cache.enums.ProductCacheConfigEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class ProductCacheRepository {

    private static final Logger log = LoggerFactory.getLogger(ProductCacheRepository.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper objectMapper;

    public Optional<Product> getByProductId(Long productId) {
        try {
            String json = stringRedisTemplate.opsForValue().get(cacheKey(productId));
            if (json == null || json.isEmpty()) {
                return Optional.empty();
            }
            Product product = objectMapper.readValue(json, Product.class);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            log.warn("Load product cache failed, productId={}", productId, e);
            return Optional.empty();
        }
    }

    public void put(Product product) {
        if (product == null || product.getProductId() == null) {
            return;
        }
        try {
            String json = objectMapper.writeValueAsString(product);
            stringRedisTemplate.opsForValue().set(
                    cacheKey(product.getProductId()),
                    json,
                    ProductCacheConfigEnum.PRODUCT_INFO.getTtl()
            );
        } catch (Exception e) {
            log.warn("Save product cache failed, productId={}", product.getProductId(), e);
        }
    }

    private String cacheKey(Long productId) {
        return ProductCacheConfigEnum.PRODUCT_INFO.getKeyPrefix() + productId;
    }
}
