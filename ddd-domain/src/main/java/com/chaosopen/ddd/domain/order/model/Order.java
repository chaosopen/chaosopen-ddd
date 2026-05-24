package com.chaosopen.ddd.domain.order.model;

import com.chaosopen.ddd.common.enums.ErrorCode;
import com.chaosopen.ddd.common.exception.BizException;
import com.chaosopen.ddd.domain.order.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 订单聚合根。
 */
@Data
public class Order {

    /**
     * 订单ID。
     */
    private Long orderId;
    /**
     * 订单号。
     */
    private String orderNo;
    /**
     * 用户ID。
     */
    private Long userId;
    /**
     * 门店ID。
     */
    private Long storeId;
    /**
     * 订单状态。
     */
    private OrderStatus status;
    /**
     * 订单总金额。
     */
    private BigDecimal totalAmount;
    /**
     * 创建时间。
     */
    private LocalDateTime createdAt;
    /**
     * 支付时间。
     */
    private LocalDateTime paidAt;
    /**
     * 取消时间。
     */
    private LocalDateTime cancelledAt;
    /**
     * 订单项列表。
     */
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    /**
     * 初始化订单。
     *
     * @param userId 用户ID
     * @param storeId 门店ID
     * @return 订单对象
     */
    public static Order init(Long userId, Long storeId) {
        if (userId == null || storeId == null) {
            throw new BizException(ErrorCode.FAILED, "用户和门店信息不能为空");
        }
        Order order = new Order();
        order.setOrderNo("O" + UUID.randomUUID().toString().replace("-", ""));
        order.setUserId(userId);
        order.setStoreId(storeId);
        order.setStatus(OrderStatus.INIT);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }

    /**
     * 添加订单项。
     *
     * @param item 订单项
     */
    public void addItem(OrderItem item) {
        ensureStatus(OrderStatus.INIT);
        if (item == null) {
            throw new BizException(ErrorCode.FAILED, "订单项不能为空");
        }
        item.setOrderNo(this.orderNo);
        item.recalculateLineAmount();
        orderItems.add(item);
        recalculateTotalAmount();
    }

    /**
     * 获取只读订单项列表。
     *
     * @return 订单项列表
     */
    public List<OrderItem> itemList() {
        return Collections.unmodifiableList(orderItems);
    }

    /**
     * 提交订单，流转到待支付。
     */
    public void submit() {
        ensureStatus(OrderStatus.INIT);
        if (orderItems.isEmpty()) {
            throw new BizException(ErrorCode.FAILED, "订单项不能为空");
        }
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException(ErrorCode.FAILED, "订单总金额必须大于0");
        }
        this.status = OrderStatus.PENDING_PAYMENT;
    }

    /**
     * 订单支付成功。
     */
    public void pay() {
        ensureStatus(OrderStatus.PENDING_PAYMENT);
        this.status = OrderStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    /**
     * 订单发货。
     */
    public void ship() {
        ensureStatus(OrderStatus.PAID);
        this.status = OrderStatus.SHIPPED;
    }

    /**
     * 取消订单。
     */
    public void cancel() {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.CANCELLED || status == OrderStatus.CLOSED) {
            throw new BizException(ErrorCode.ORDER_STATUS_INVALID, "当前状态不允许取消");
        }
        this.status = OrderStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    /**
     * 重新计算订单总金额。
     */
    private void recalculateTotalAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getLineAmount() != null) {
                amount = amount.add(orderItem.getLineAmount());
            }
        }
        this.totalAmount = amount;
    }

    private void ensureStatus(OrderStatus expectedStatus) {
        if (status != expectedStatus) {
            throw new BizException(ErrorCode.ORDER_STATUS_INVALID,
                    "状态流转不合法，期望:" + expectedStatus.getDesc() + "，当前:" + status.getDesc());
        }
    }
}
