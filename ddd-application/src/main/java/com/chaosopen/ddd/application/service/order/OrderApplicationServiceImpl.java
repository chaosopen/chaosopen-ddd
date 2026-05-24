package com.chaosopen.ddd.application.service.order;

import com.chaosopen.ddd.application.command.order.CreateOrderCmdExe;
import com.chaosopen.ddd.client.api.OrderServiceI;
import com.chaosopen.ddd.client.dto.CreateOrderCmd;
import com.chaosopen.ddd.client.dto.clientobject.CreateOrderCO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderApplicationServiceImpl implements OrderServiceI {

    @Autowired
    private CreateOrderCmdExe createOrderCmdExe;

    @Override
    public CreateOrderCO createOrder(CreateOrderCmd cmd) {
        return createOrderCmdExe.execute(cmd);
    }
}
