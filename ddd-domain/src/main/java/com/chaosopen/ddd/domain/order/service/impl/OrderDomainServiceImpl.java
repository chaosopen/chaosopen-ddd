package com.chaosopen.ddd.domain.order.service.impl;

import com.chaosopen.ddd.common.enums.ErrorCode;
import com.chaosopen.ddd.common.exception.BizException;
import com.chaosopen.ddd.domain.event.DomainEventPublisher;
import com.chaosopen.ddd.domain.inventory.gateway.InventoryGateway;
import com.chaosopen.ddd.domain.inventory.model.Inventory;
import com.chaosopen.ddd.domain.order.event.OrderCreatedDomainEvent;
import com.chaosopen.ddd.domain.order.gateway.OrderGateway;
import com.chaosopen.ddd.domain.order.model.Order;
import com.chaosopen.ddd.domain.order.model.OrderItem;
import com.chaosopen.ddd.domain.order.service.OrderDomainService;
import com.chaosopen.ddd.domain.order.dto.CreateOrderResult;
import com.chaosopen.ddd.domain.product.gateway.ProductGateway;
import com.chaosopen.ddd.domain.product.gateway.SkuGateway;
import com.chaosopen.ddd.domain.product.model.Product;
import com.chaosopen.ddd.domain.product.model.Sku;
import com.chaosopen.ddd.domain.user.gateway.UserGateway;
import com.chaosopen.ddd.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单领域服务默认实现。
 * 说明：下单全流程规则在此内聚，应用层只负责触发与结果返回。
 */
public class OrderDomainServiceImpl implements OrderDomainService {

    private SkuGateway skuGateway;
    private InventoryGateway inventoryGateway;
    private ProductGateway productGateway;
    private UserGateway userGateway;
    private OrderGateway orderGateway;

    private DomainEventPublisher domainEventPublisher;

    public void setSkuGateway(SkuGateway skuGateway) {
        this.skuGateway = skuGateway;
    }

    public void setInventoryGateway(InventoryGateway inventoryGateway) {
        this.inventoryGateway = inventoryGateway;
    }

    public void setProductGateway(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public void setUserGateway(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void setOrderGateway(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public void setDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public CreateOrderResult placeOrder(Long userId, Long storeId, List<OrderItem> items) {
        // 1. 基础参数校验与用户校验
        validateOrderItems(items);
        User user = loadUser(userId);

        List<Inventory> updatedInventories = new ArrayList<Inventory>();
        List<Product> updatedProducts = new ArrayList<Product>();
        Map<Long, Integer> productSalesDelta = new HashMap<Long, Integer>();
        int totalQuantity = 0;

        // 2. 逐条校验 SKU/库存并执行扣减，顺便累计销量增量与积分增量
        for (OrderItem item : items) {
            Sku sku = loadAndValidateSku(item.getSkuId());
            Inventory inventory = loadAndDeductInventory(storeId, item);
            updatedInventories.add(inventory);
            collectSalesDelta(productSalesDelta, sku.getProductId(), item.getQuantity());
            totalQuantity += item.getQuantity();
        }

        // 3. 聚合变更（销量、积分）并生成订单
        applyProductSales(productSalesDelta, updatedProducts);
        user.addPoints(totalQuantity);

        Order order = buildAndSubmitOrder(userId, storeId, items);

        // 4. 统一持久化，保证规则执行后的状态落库
        persistChanges(order, updatedInventories, updatedProducts, user);
        // 5. 发布事件
        OrderCreatedDomainEvent orderCreatedEvent = buildOrderCreatedEvent(order, user);
        domainEventPublisher.publish(orderCreatedEvent);
        return new CreateOrderResult(order, updatedInventories, updatedProducts, user);
    }

    /**
     * 校验订单项非空。
     */
    private void validateOrderItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new BizException(ErrorCode.FAILED, "创建订单时订单项不能为空");
        }
    }

    /**
     * 加载并校验用户是否存在。
     */
    private User loadUser(Long userId) {
        return userGateway.findByUserId(userId)
                .orElseThrow(() -> new BizException(ErrorCode.USER_NOT_FOUND, "用户不存在"));
    }

    /**
     * 加载 SKU 并校验是否上架。
     */
    private Sku loadAndValidateSku(Long skuId) {
        Sku sku = skuGateway.findBySkuId(skuId)
                .orElseThrow(() -> new BizException(ErrorCode.SKU_NOT_FOUND, "SKU不存在"));
        if (!sku.isOnShelf()) {
            throw new BizException(ErrorCode.PRODUCT_OFF_SHELF, "商品未上架，不能下单");
        }
        return sku;
    }

    /**
     * 加载库存并执行扣减。
     */
    private Inventory loadAndDeductInventory(Long storeId, OrderItem item) {
        Inventory inventory = inventoryGateway.findByStoreIdAndSkuId(storeId, item.getSkuId())
                .orElseThrow(() -> new BizException(ErrorCode.INVENTORY_OVER_SOLD, "门店商品库存不存在"));
        if (inventory.getAvailableStock() == null || inventory.getAvailableStock() <= 0) {
            throw new BizException(ErrorCode.INVENTORY_OVER_SOLD, "门店商品库存为0，不能下单");
        }
        if (!inventory.lock(item.getQuantity())) {
            throw new BizException(ErrorCode.INVENTORY_LOCK_FAILED, "库存不足，扣减失败");
        }
        return inventory;
    }

    /**
     * 累积每个商品的销量增量。
     */
    private void collectSalesDelta(Map<Long, Integer> productSalesDelta, Long productId, Integer quantity) {
        Integer current = productSalesDelta.containsKey(productId) ? productSalesDelta.get(productId) : 0;
        productSalesDelta.put(productId, current + quantity);
    }

    /**
     * 按商品维度应用销量增量。
     */
    private void applyProductSales(Map<Long, Integer> productSalesDelta, List<Product> updatedProducts) {
        for (Map.Entry<Long, Integer> entry : productSalesDelta.entrySet()) {
            Product product = productGateway.findByProductId(entry.getKey())
                    .orElseThrow(() -> new BizException(ErrorCode.PRODUCT_NOT_FOUND, "商品不存在"));
            product.increaseSales(entry.getValue());
            updatedProducts.add(product);
        }
    }

    /**
     * 构建并提交订单（状态流转到待支付）。
     */
    private Order buildAndSubmitOrder(Long userId, Long storeId, List<OrderItem> items) {
        Order order = Order.init(userId, storeId);
        for (OrderItem item : items) {
            order.addItem(item);
        }
        order.submit();
        return order;
    }

    /**
     * 持久化本次下单涉及的全部聚合变更。
     */
    private void persistChanges(Order order,
                                List<Inventory> updatedInventories,
                                List<Product> updatedProducts,
                                User user) {
        orderGateway.save(order);
        for (Inventory inventory : updatedInventories) {
            inventoryGateway.save(inventory);
        }
        for (Product product : updatedProducts) {
            productGateway.save(product);
        }
        userGateway.save(user);
    }

    /**
     * 构建订单创建领域事件。
     */
    private OrderCreatedDomainEvent buildOrderCreatedEvent(Order order, User user) {
        OrderCreatedDomainEvent event = new OrderCreatedDomainEvent();
        event.setOrderId(order.getOrderId());
        event.setOrderNo(order.getOrderNo());
        event.setUserId(order.getUserId());
        if (user != null) {
            event.setMobile(user.getMobile());
        }
        event.setTotalAmount(order.getTotalAmount());
        event.setStoreId(order.getStoreId());
        event.setCreateTime(LocalDateTime.now());
        return event;
    }
}
