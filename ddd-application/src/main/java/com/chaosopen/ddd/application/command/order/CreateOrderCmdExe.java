package com.chaosopen.ddd.application.command.order;

import com.chaosopen.ddd.application.converter.order.OrderApplicationConverter;
import com.chaosopen.ddd.client.dto.CreateOrderCmd;
import com.chaosopen.ddd.client.dto.clientobject.CreateOrderCO;
import com.chaosopen.ddd.domain.inventory.service.InventoryDomainService;
import com.chaosopen.ddd.domain.order.dto.CreateOrderResult;
import com.chaosopen.ddd.domain.order.model.OrderItem;
import com.chaosopen.ddd.domain.order.service.OrderDomainService;
import com.chaosopen.ddd.domain.product.dto.ProductOrderData;
import com.chaosopen.ddd.domain.product.service.ProductDomainService;
import com.chaosopen.ddd.domain.store.model.Store;
import com.chaosopen.ddd.domain.user.model.User;
import com.chaosopen.ddd.domain.user.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CreateOrderCmdExe {

    @Autowired
    private OrderDomainService orderDomainService;
    @Autowired
    private ProductDomainService productDomainService;
    @Autowired
    private InventoryDomainService inventoryDomainService;
    @Autowired
    private UserDomainService userDomainService;

    /**
     * 下单应用编排：
     * 1) DTO 转领域对象
     * 2) 编排商品/库存/用户领域规则
     * 3) 调用订单领域服务创建订单并发布订单事件
     * 4) 转换返回结果
     */
    @Transactional(rollbackFor = Exception.class)
    public CreateOrderCO execute(CreateOrderCmd cmd) {
        List<OrderItem> items = OrderApplicationConverter.toOrderItems(cmd);
        User user = userDomainService.getByUserId(cmd.getUserId());
        Store store = buildStore(cmd.getStoreId());
        ProductOrderData productOrderData = productDomainService.validateSkusAndCollectOrderData(items);
        inventoryDomainService.deductAndSaveBatch(store, items);
        productDomainService.increaseSalesAndSave(productOrderData.getProductSalesDelta());
        CreateOrderResult result = orderDomainService.placeOrder(items, user, store, productOrderData.getSkuMap());
        userDomainService.addPointsAndSave(user, result.getOrderTotalQuantity());
        return OrderApplicationConverter.toCreateOrderCO(result.getOrder());
    }

    private Store buildStore(Long storeId) {
        Store store = new Store();
        store.setStoreId(storeId);
        return store;
    }
}
