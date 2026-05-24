package com.chaosopen.ddd.application.config;

import com.chaosopen.ddd.domain.event.DomainEventPublisher;
import com.chaosopen.ddd.domain.inventory.gateway.InventoryGateway;
import com.chaosopen.ddd.domain.order.gateway.OrderGateway;
import com.chaosopen.ddd.domain.order.service.OrderDomainService;
import com.chaosopen.ddd.domain.order.service.impl.OrderDomainServiceImpl;
import com.chaosopen.ddd.domain.product.gateway.ProductGateway;
import com.chaosopen.ddd.domain.product.gateway.SkuGateway;
import com.chaosopen.ddd.domain.user.gateway.UserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 领域服务装配配置。
 */
@Configuration
public class DomainServiceConfig {

    /**
     * 订单领域服务装配。
     */
    @Bean
    public OrderDomainService orderDomainService(SkuGateway skuGateway,
                                                 InventoryGateway inventoryGateway,
                                                 ProductGateway productGateway,
                                                 UserGateway userGateway,
                                                 OrderGateway orderGateway,
                                                 DomainEventPublisher domainEventPublisher) {
        OrderDomainServiceImpl service = new OrderDomainServiceImpl();
        service.setSkuGateway(skuGateway);
        service.setInventoryGateway(inventoryGateway);
        service.setProductGateway(productGateway);
        service.setUserGateway(userGateway);
        service.setOrderGateway(orderGateway);
        service.setDomainEventPublisher(domainEventPublisher);
        return service;
    }
}
