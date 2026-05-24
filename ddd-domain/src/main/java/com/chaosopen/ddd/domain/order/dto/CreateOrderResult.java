package com.chaosopen.ddd.domain.order.dto;

import com.chaosopen.ddd.domain.inventory.model.Inventory;
import com.chaosopen.ddd.domain.order.model.Order;
import com.chaosopen.ddd.domain.product.model.Product;
import com.chaosopen.ddd.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateOrderResult {

    private Order order;
    private List<Inventory> updatedInventories;
    private List<Product> updatedProducts;
    private User updatedUser;
}
