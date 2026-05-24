package com.chaosopen.ddd.adapter.controller.order;

import com.chaosopen.ddd.client.api.OrderServiceI;
import com.chaosopen.ddd.client.dto.CreateOrderCmd;
import com.chaosopen.ddd.client.dto.clientobject.CreateOrderCO;
import com.chaosopen.ddd.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderServiceI orderService;

    @PostMapping
    public Result<CreateOrderCO> createOrder(@RequestBody CreateOrderCmd cmd) {
        return Result.success(orderService.createOrder(cmd));
    }
}
