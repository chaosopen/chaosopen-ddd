package com.chaosopen.ddd.client.dto.clientobject;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductInfoCO {

    private Long productId;
    private String productName;
    private Integer shelfStatus;
    private String shelfStatusDesc;
    private Integer salesCount;
    private List<SkuCO> skuList;

    @Data
    @AllArgsConstructor
    public static class SkuCO {
        private Long skuId;
        private String skuCode;
        private String skuName;
        private BigDecimal salePrice;
        private Integer shelfStatus;
        private String shelfStatusDesc;
    }
}
