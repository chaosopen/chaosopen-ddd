package com.chaosopen.ddd.application.command.order;

import com.chaosopen.ddd.application.converter.order.OrderApplicationConverter;
import com.chaosopen.ddd.client.dto.CreateOrderCmd;
import com.chaosopen.ddd.client.dto.clientobject.CreateOrderCO;
import com.chaosopen.ddd.domain.order.model.OrderItem;
import com.chaosopen.ddd.domain.order.service.OrderDomainService;
import com.chaosopen.ddd.domain.order.dto.CreateOrderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CreateOrderCmdExe {

    @Autowired
    private OrderDomainService orderDomainService;

    /**
     * 下单应用编排：
     * 1) DTO 转领域对象
     * 2) 调用领域服务执行业务规则
     * 3) 领域服务内部发布领域事件（由事件处理器异步投递短信MQ）
     * 4) 转换返回结果
     */
    @Transactional(rollbackFor = Exception.class)
    public CreateOrderCO execute(CreateOrderCmd cmd) {
        List<OrderItem> items = OrderApplicationConverter.toOrderItems(cmd);
        CreateOrderResult result = orderDomainService.placeOrder(cmd.getUserId(), cmd.getStoreId(), items);
        return OrderApplicationConverter.toCreateOrderCO(result.getOrder());
    }
}
