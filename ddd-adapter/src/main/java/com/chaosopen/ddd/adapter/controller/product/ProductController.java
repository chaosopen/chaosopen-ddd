package com.chaosopen.ddd.adapter.controller.product;

import com.chaosopen.ddd.client.api.ProductServiceI;
import com.chaosopen.ddd.client.dto.ProductInfoQry;
import com.chaosopen.ddd.client.dto.clientobject.ProductInfoCO;
import com.chaosopen.ddd.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceI productService;

    @GetMapping("/{productId}")
    public Result<ProductInfoCO> getProductInfo(@PathVariable("productId") Long productId) {
        ProductInfoQry qry = new ProductInfoQry();
        qry.setProductId(productId);
        return Result.success(productService.getProductInfo(qry));
    }
}
