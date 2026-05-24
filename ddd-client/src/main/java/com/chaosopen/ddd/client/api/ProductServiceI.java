package com.chaosopen.ddd.client.api;

import com.chaosopen.ddd.client.dto.ProductInfoQry;
import com.chaosopen.ddd.client.dto.clientobject.ProductInfoCO;

public interface ProductServiceI {

    ProductInfoCO getProductInfo(ProductInfoQry qry);
}
