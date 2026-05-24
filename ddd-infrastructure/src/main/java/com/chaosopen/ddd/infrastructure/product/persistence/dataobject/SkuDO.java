package com.chaosopen.ddd.infrastructure.product.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_sku")
public class SkuDO {

    @TableId(value = "sku_id", type = IdType.AUTO)
    private Long skuId;
    private Long productId;
    private String skuCode;
    private String skuName;
    private BigDecimal salePrice;
    private Integer shelfStatus;
}
