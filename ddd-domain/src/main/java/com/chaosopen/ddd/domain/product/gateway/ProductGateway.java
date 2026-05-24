package com.chaosopen.ddd.domain.product.gateway;

import com.chaosopen.ddd.domain.product.model.Product;

import java.util.Optional;

public interface ProductGateway {

    Optional<Product> findByProductId(Long productId);

    void save(Product product);
}
