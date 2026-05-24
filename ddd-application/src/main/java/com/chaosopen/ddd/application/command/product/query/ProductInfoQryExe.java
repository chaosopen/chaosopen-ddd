package com.chaosopen.ddd.application.command.product.query;

import com.chaosopen.ddd.application.converter.product.ProductApplicationConverter;
import com.chaosopen.ddd.client.dto.ProductInfoQry;
import com.chaosopen.ddd.client.dto.clientobject.ProductInfoCO;
import com.chaosopen.ddd.common.enums.ErrorCode;
import com.chaosopen.ddd.common.exception.BizException;
import com.chaosopen.ddd.domain.product.gateway.ProductGateway;
import com.chaosopen.ddd.domain.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductInfoQryExe {

    @Autowired
    private ProductGateway productGateway;

    public ProductInfoCO execute(ProductInfoQry qry) {
        Product product = productGateway.findByProductId(qry.getProductId())
                .orElseThrow(() -> new BizException(ErrorCode.PRODUCT_NOT_FOUND, "商品不存在"));
        return ProductApplicationConverter.toProductInfoCO(product);
    }
}
