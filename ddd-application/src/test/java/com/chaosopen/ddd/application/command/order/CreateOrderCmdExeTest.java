package com.chaosopen.ddd.application.command.order;

import com.chaosopen.ddd.client.dto.CreateOrderCmd;
import com.chaosopen.ddd.domain.event.DomainEventPublisher;
import com.chaosopen.ddd.domain.inventory.service.InventoryDomainService;
import com.chaosopen.ddd.domain.order.dto.CreateOrderResult;
import com.chaosopen.ddd.domain.order.event.OrderCreatedDomainEvent;
import com.chaosopen.ddd.domain.order.model.Order;
import com.chaosopen.ddd.domain.order.service.OrderDomainService;
import com.chaosopen.ddd.domain.product.dto.ProductOrderData;
import com.chaosopen.ddd.domain.product.service.ProductDomainService;
import com.chaosopen.ddd.domain.store.model.Store;
import com.chaosopen.ddd.domain.user.model.User;
import com.chaosopen.ddd.domain.user.service.UserDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class CreateOrderCmdExeTest {

    @Test
    void should_publish_domain_event_in_application_layer() {
        CreateOrderCmdExe exe = new CreateOrderCmdExe();
        OrderDomainService orderDomainService = Mockito.mock(OrderDomainService.class);
        ProductDomainService productDomainService = Mockito.mock(ProductDomainService.class);
        InventoryDomainService inventoryDomainService = Mockito.mock(InventoryDomainService.class);
        UserDomainService userDomainService = Mockito.mock(UserDomainService.class);
        DomainEventPublisher domainEventPublisher = Mockito.mock(DomainEventPublisher.class);

        ReflectionTestUtils.setField(exe, "orderDomainService", orderDomainService);
        ReflectionTestUtils.setField(exe, "productDomainService", productDomainService);
        ReflectionTestUtils.setField(exe, "inventoryDomainService", inventoryDomainService);
        ReflectionTestUtils.setField(exe, "userDomainService", userDomainService);
        ReflectionTestUtils.setField(exe, "domainEventPublisher", domainEventPublisher);

        CreateOrderCmd cmd = new CreateOrderCmd();
        cmd.setUserId(1L);
        cmd.setStoreId(2L);
        CreateOrderCmd.OrderItemCmd itemCmd = new CreateOrderCmd.OrderItemCmd();
        itemCmd.setSkuId(100L);
        itemCmd.setSkuName("sku-a");
        itemCmd.setQuantity(2);
        itemCmd.setUnitPrice(new BigDecimal("10.00"));
        cmd.setItems(Arrays.asList(itemCmd));

        User user = new User();
        user.setUserId(1L);
        user.setMobile("13800000000");
        Mockito.when(userDomainService.getByUserId(1L)).thenReturn(user);
        Mockito.when(productDomainService.validateSkusAndCollectOrderData(ArgumentMatchers.anyList()))
                .thenReturn(new ProductOrderData(new HashMap<Long, Integer>(), new HashMap<Long, com.chaosopen.ddd.domain.product.model.Sku>()));

        Order order = new Order();
        order.setOrderNo("O-test");
        order.setTotalAmount(new BigDecimal("20.00"));
        order.setStatus(com.chaosopen.ddd.domain.order.enums.OrderStatus.PENDING_PAYMENT);
        OrderCreatedDomainEvent event = new OrderCreatedDomainEvent();
        order.registerDomainEvent(event);
        Mockito.when(orderDomainService.placeOrder(ArgumentMatchers.anyList(), ArgumentMatchers.any(User.class),
                        ArgumentMatchers.any(Store.class), ArgumentMatchers.anyMap()))
                .thenReturn(new CreateOrderResult(order, 2));

        exe.execute(cmd);

        Mockito.verify(domainEventPublisher, Mockito.times(1))
                .publish(ArgumentMatchers.argThat(events ->
                        events != null
                                && events.size() == 1
                                && events.get(0) == event));
    }

    @Test
    void should_not_publish_when_place_order_failed() {
        CreateOrderCmdExe exe = new CreateOrderCmdExe();
        OrderDomainService orderDomainService = Mockito.mock(OrderDomainService.class);
        ProductDomainService productDomainService = Mockito.mock(ProductDomainService.class);
        InventoryDomainService inventoryDomainService = Mockito.mock(InventoryDomainService.class);
        UserDomainService userDomainService = Mockito.mock(UserDomainService.class);
        DomainEventPublisher domainEventPublisher = Mockito.mock(DomainEventPublisher.class);

        ReflectionTestUtils.setField(exe, "orderDomainService", orderDomainService);
        ReflectionTestUtils.setField(exe, "productDomainService", productDomainService);
        ReflectionTestUtils.setField(exe, "inventoryDomainService", inventoryDomainService);
        ReflectionTestUtils.setField(exe, "userDomainService", userDomainService);
        ReflectionTestUtils.setField(exe, "domainEventPublisher", domainEventPublisher);

        CreateOrderCmd cmd = new CreateOrderCmd();
        cmd.setUserId(1L);
        cmd.setStoreId(2L);
        CreateOrderCmd.OrderItemCmd itemCmd = new CreateOrderCmd.OrderItemCmd();
        itemCmd.setSkuId(100L);
        itemCmd.setSkuName("sku-a");
        itemCmd.setQuantity(2);
        itemCmd.setUnitPrice(new BigDecimal("10.00"));
        cmd.setItems(Arrays.asList(itemCmd));

        User user = new User();
        user.setUserId(1L);
        user.setMobile("13800000000");
        Mockito.when(userDomainService.getByUserId(1L)).thenReturn(user);
        Mockito.when(productDomainService.validateSkusAndCollectOrderData(ArgumentMatchers.anyList()))
                .thenReturn(new ProductOrderData(new HashMap<Long, Integer>(), new HashMap<Long, com.chaosopen.ddd.domain.product.model.Sku>()));
        Mockito.when(orderDomainService.placeOrder(ArgumentMatchers.anyList(), ArgumentMatchers.any(User.class),
                        ArgumentMatchers.any(Store.class), ArgumentMatchers.anyMap()))
                .thenThrow(new RuntimeException("place order failed"));

        Assertions.assertThrows(RuntimeException.class, () -> exe.execute(cmd));
        Mockito.verify(domainEventPublisher, Mockito.never()).publish(ArgumentMatchers.any());
    }
}
