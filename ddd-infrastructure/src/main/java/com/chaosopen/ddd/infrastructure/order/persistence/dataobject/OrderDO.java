package com.chaosopen.ddd.infrastructure.order.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_order")
public class OrderDO {

    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;
    private String orderNo;
    private Long userId;
    private Long storeId;
    private Integer status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;
}
