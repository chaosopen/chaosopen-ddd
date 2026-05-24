package com.chaosopen.ddd.client.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderCmd {

    private Long userId;
    private Long storeId;
    private List<OrderItemCmd> items;

    @Data
    public static class OrderItemCmd {
        private Long skuId;
        private String skuName;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
