package com.chaosopen.ddd.infrastructure.product.gatewayImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaosopen.ddd.domain.product.gateway.ProductGateway;
import com.chaosopen.ddd.domain.product.enums.ProductShelfStatus;
import com.chaosopen.ddd.domain.product.model.Product;
import com.chaosopen.ddd.domain.product.model.Sku;
import com.chaosopen.ddd.infrastructure.product.cache.ProductCacheRepository;
import com.chaosopen.ddd.infrastructure.product.persistence.mapper.ProductMapper;
import com.chaosopen.ddd.infrastructure.product.persistence.mapper.SkuMapper;
import com.chaosopen.ddd.infrastructure.product.persistence.dataobject.ProductDO;
import com.chaosopen.ddd.infrastructure.product.persistence.dataobject.SkuDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class ProductGatewayImpl implements ProductGateway {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private ProductCacheRepository productCacheRepository;

    @Override
    public Optional<Product> findByProductId(Long productId) {
        Optional<Product> cacheProduct = productCacheRepository.getByProductId(productId);
        if (cacheProduct.isPresent()) {
            return cacheProduct;
        }

        ProductDO productDO = productMapper.selectById(productId);
        if (productDO == null) {
            return Optional.empty();
        }

        LambdaQueryWrapper<SkuDO> queryWrapper = new LambdaQueryWrapper<SkuDO>()
                .eq(SkuDO::getProductId, productId);
        List<SkuDO> skuDOList = skuMapper.selectList(queryWrapper);
        Product product = toProduct(productDO, skuDOList);
        productCacheRepository.put(product);
        return Optional.of(product);
    }

    @Override
    public void save(Product product) {
        ProductDO productDO = new ProductDO();
        productDO.setProductId(product.getProductId());
        productDO.setProductName(product.getProductName());
        productDO.setShelfStatus(product.getShelfStatus() == null ? null : product.getShelfStatus().getValue());
        productDO.setSalesCount(product.getSalesCount());

        if (productDO.getProductId() == null) {
            productMapper.insert(productDO);
            product.setProductId(productDO.getProductId());
        } else {
            productMapper.updateById(productDO);
        }

        for (Sku sku : product.skuList()) {
            SkuDO skuDO = new SkuDO();
            skuDO.setSkuId(sku.getSkuId());
            skuDO.setProductId(product.getProductId());
            skuDO.setSkuCode(sku.getSkuCode());
            skuDO.setSkuName(sku.getSkuName());
            skuDO.setSalePrice(sku.getSalePrice());
            skuDO.setShelfStatus(sku.getShelfStatus() == null ? null : sku.getShelfStatus().getValue());
            if (skuDO.getSkuId() == null) {
                skuMapper.insert(skuDO);
                sku.setSkuId(skuDO.getSkuId());
            } else {
                skuMapper.updateById(skuDO);
            }
        }
    }

    private ProductShelfStatus resolveShelfStatus(Integer value) {
        if (value == null) {
            return null;
        }
        return value == ProductShelfStatus.ON_SHELF.getValue() ? ProductShelfStatus.ON_SHELF : ProductShelfStatus.OFF_SHELF;
    }

    private Product toProduct(ProductDO productDO, List<SkuDO> skuDOList) {
        Product product = new Product();
        product.setProductId(productDO.getProductId());
        product.setProductName(productDO.getProductName());
        product.setShelfStatus(resolveShelfStatus(productDO.getShelfStatus()));
        product.setSalesCount(productDO.getSalesCount());

        for (SkuDO skuDO : skuDOList) {
            Sku sku = new Sku();
            sku.setSkuId(skuDO.getSkuId());
            sku.setProductId(skuDO.getProductId());
            sku.setSkuCode(skuDO.getSkuCode());
            sku.setSkuName(skuDO.getSkuName());
            sku.setSalePrice(skuDO.getSalePrice());
            sku.setShelfStatus(resolveShelfStatus(skuDO.getShelfStatus()));
            product.addSku(sku);
        }
        return product;
    }

}
