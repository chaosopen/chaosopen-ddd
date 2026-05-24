package com.chaosopen.ddd.infrastructure.order.gatewayImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaosopen.ddd.domain.order.gateway.OrderGateway;
import com.chaosopen.ddd.domain.order.model.Order;
import com.chaosopen.ddd.infrastructure.order.converter.OrderInfrastructureConverter;
import com.chaosopen.ddd.infrastructure.order.persistence.mapper.OrderItemMapper;
import com.chaosopen.ddd.infrastructure.order.persistence.mapper.OrderMapper;
import com.chaosopen.ddd.infrastructure.order.persistence.dataobject.OrderDO;
import com.chaosopen.ddd.infrastructure.order.persistence.dataobject.OrderItemDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class OrderGatewayImpl implements OrderGateway {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Override
    public void save(Order order) {
        OrderDO orderDO = OrderInfrastructureConverter.toOrderDO(order);
        orderMapper.insert(orderDO);

        List<OrderItemDO> itemDOs = OrderInfrastructureConverter.toOrderItemDOs(order);
        for (OrderItemDO itemDO : itemDOs) {
            orderItemMapper.insert(itemDO);
        }
        order.setOrderId(orderDO.getOrderId());
    }

    @Override
    public Optional<Order> findByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderDO> orderQw = new LambdaQueryWrapper<OrderDO>()
                .eq(OrderDO::getOrderNo, orderNo)
                .last("limit 1");
        OrderDO orderDO = orderMapper.selectOne(orderQw);
        if (orderDO == null) {
            return Optional.empty();
        }

        LambdaQueryWrapper<OrderItemDO> itemQw = new LambdaQueryWrapper<OrderItemDO>()
                .eq(OrderItemDO::getOrderNo, orderNo);
        List<OrderItemDO> itemDOs = orderItemMapper.selectList(itemQw);
        return Optional.of(OrderInfrastructureConverter.toOrder(orderDO, itemDOs));
    }
}
