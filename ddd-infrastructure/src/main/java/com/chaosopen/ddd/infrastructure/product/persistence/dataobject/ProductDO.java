package com.chaosopen.ddd.infrastructure.product.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_product")
public class ProductDO {

    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;
    private String productName;
    private Integer shelfStatus;
    private Integer salesCount;
}
