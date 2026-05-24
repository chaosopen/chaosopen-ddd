package com.chaosopen.ddd.client.api;

import com.chaosopen.ddd.client.dto.CreateOrderCmd;
import com.chaosopen.ddd.client.dto.clientobject.CreateOrderCO;

public interface OrderServiceI {

    CreateOrderCO createOrder(CreateOrderCmd cmd);
}
