package com.chaosopen.ddd.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.chaosopen.ddd.domain.store.model.Store;
import com.chaosopen.ddd.domain.user.model.User;

import java.util.List;

/**
 * 下单信息值对象：由订单领域服务基于多领域实体组装。
 */
@Data
@AllArgsConstructor
public class OrderCreateInfo {

    private User user;
    private Store store;
    private List<OrderItem> orderItems;
}
