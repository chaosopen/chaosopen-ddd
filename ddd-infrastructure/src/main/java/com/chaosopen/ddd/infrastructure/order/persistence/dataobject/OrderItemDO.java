package com.chaosopen.ddd.infrastructure.order.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_order_item")
public class OrderItemDO {

    @TableId(value = "order_item_id", type = IdType.AUTO)
    private Long orderItemId;
    private String orderNo;
    private Long skuId;
    private String skuName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineAmount;
}
