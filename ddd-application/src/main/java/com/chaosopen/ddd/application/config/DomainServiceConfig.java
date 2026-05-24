package com.chaosopen.ddd.application.config;

import com.chaosopen.ddd.domain.event.DomainEventPublisher;
import com.chaosopen.ddd.domain.inventory.gateway.InventoryGateway;
import com.chaosopen.ddd.domain.inventory.service.InventoryDomainService;
import com.chaosopen.ddd.domain.inventory.service.impl.InventoryDomainServiceImpl;
import com.chaosopen.ddd.domain.order.gateway.OrderGateway;
import com.chaosopen.ddd.domain.order.service.OrderDomainService;
import com.chaosopen.ddd.domain.order.service.impl.OrderDomainServiceImpl;
import com.chaosopen.ddd.domain.product.gateway.ProductGateway;
import com.chaosopen.ddd.domain.product.gateway.SkuGateway;
import com.chaosopen.ddd.domain.product.service.ProductDomainService;
import com.chaosopen.ddd.domain.product.service.impl.ProductDomainServiceImpl;
import com.chaosopen.ddd.domain.user.gateway.UserGateway;
import com.chaosopen.ddd.domain.user.service.UserDomainService;
import com.chaosopen.ddd.domain.user.service.impl.UserDomainServiceImpl;
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
    public OrderDomainService orderDomainService(OrderGateway orderGateway,
                                                 DomainEventPublisher domainEventPublisher) {
        OrderDomainServiceImpl service = new OrderDomainServiceImpl();
        service.setOrderGateway(orderGateway);
        service.setDomainEventPublisher(domainEventPublisher);
        return service;
    }

    /**
     * 商品领域服务装配。
     */
    @Bean
    public ProductDomainService productDomainService(SkuGateway skuGateway, ProductGateway productGateway) {
        ProductDomainServiceImpl service = new ProductDomainServiceImpl();
        service.setSkuGateway(skuGateway);
        service.setProductGateway(productGateway);
        return service;
    }

    /**
     * 库存领域服务装配。
     */
    @Bean
    public InventoryDomainService inventoryDomainService(InventoryGateway inventoryGateway) {
        InventoryDomainServiceImpl service = new InventoryDomainServiceImpl();
        service.setInventoryGateway(inventoryGateway);
        return service;
    }

    /**
     * 用户领域服务装配。
     */
    @Bean
    public UserDomainService userDomainService(UserGateway userGateway) {
        UserDomainServiceImpl service = new UserDomainServiceImpl();
        service.setUserGateway(userGateway);
        return service;
    }
}
