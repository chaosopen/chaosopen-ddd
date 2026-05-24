package com.chaosopen.ddd.client.dto.clientobject;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateOrderCO {

    private String orderNo;
    private Integer status;
    private String statusDesc;
    private BigDecimal totalAmount;
}
