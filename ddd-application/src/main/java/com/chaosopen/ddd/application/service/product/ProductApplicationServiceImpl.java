package com.chaosopen.ddd.application.service.product;

import com.chaosopen.ddd.application.command.product.query.ProductInfoQryExe;
import com.chaosopen.ddd.client.api.ProductServiceI;
import com.chaosopen.ddd.client.dto.ProductInfoQry;
import com.chaosopen.ddd.client.dto.clientobject.ProductInfoCO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductApplicationServiceImpl implements ProductServiceI {

    @Autowired
    private ProductInfoQryExe productInfoQryExe;

    @Override
    public ProductInfoCO getProductInfo(ProductInfoQry qry) {
        return productInfoQryExe.execute(qry);
    }
}
