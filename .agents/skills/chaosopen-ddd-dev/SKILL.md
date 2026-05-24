# chaosopen-ddd-dev

用于 `chaosopen-ddd` 项目的 DDD 落地执行技能。

目标：让任何 AI 在不熟悉项目的情况下，也能稳定写出与本项目一致的分层代码，且符合 DDD 边界。

---

## 0. 使用方式（强制）

当任务涉及本仓库代码改动时，必须按以下顺序执行：
1. 先做“分层落位判断”（见第 1 节）。
2. 再做“链路设计”（见第 2 节）。
3. 按“模块模板”写代码（见第 4 节）。
4. 用“反模式清单”自检（见第 5 节）。
5. 按“输出模板”汇报（见第 7 节）。

禁止跳步直接写代码。

---

## 1. 分层落位判断（先判断再编码）

先判断新增逻辑属于哪类：
- 协议接入：HTTP/MQ 入参解析、响应组装 -> `ddd-adapter`
- 用例编排：事务、跨领域调用顺序、调用拼装 -> `ddd-application`
- 业务规则：状态流转、金额计算、业务约束、领域事实 -> `ddd-domain`
- 技术实现：DB/MQ/缓存/三方 SDK -> `ddd-infrastructure`
- 对外契约：`Cmd/Qry/CO/MQ DTO/API` -> `ddd-client`
- 通用能力：错误码、异常、常量、工具 -> `ddd-common`

若无法判断，优先放入：
- “规则”进 `domain`
- “流程”进 `application`

---

## 2. 固定链路与依赖方向

固定调用链：
- `adapter -> application -> domain`
- `infrastructure -> domain`（实现 domain 抽象）

硬性依赖约束：
- `domain` 禁止依赖 `adapter/infrastructure`
- `application` 禁止依赖 `infrastructure` 具体实现类（简单读模型除外）
- `adapter` 禁止编排领域流程和业务规则

项目对照文件：
- 接口入口：`ddd-adapter/src/main/java/com/chaosopen/ddd/adapter/controller/order/OrderController.java`
- 用例编排：`ddd-application/src/main/java/com/chaosopen/ddd/application/command/order/CreateOrderCmdExe.java`
- 聚合规则：`ddd-domain/src/main/java/com/chaosopen/ddd/domain/order/model/Order.java`
- 领域服务：`ddd-domain/src/main/java/com/chaosopen/ddd/domain/order/service/impl/OrderDomainServiceImpl.java`
- 事件发布实现：`ddd-infrastructure/src/main/java/com/chaosopen/ddd/infrastructure/common/event/DomainEventPublisherImpl.java`

---

## 3. 对象链路与转换位置

对象类型：
- 外部传输：`ddd-client` 下 `Cmd/Qry/CO/MQ DTO`
- 领域语义：`ddd-domain` 下 `model/event/service/gateway`
- 存储映射：`ddd-infrastructure` 下 `DO/Mapper`

转换只能发生在：
- `application/converter`：`DTO/CO <-> Domain`
- `infrastructure/*`：`Domain <-> DO/MQ DTO/Remote DTO`

强约束：
- DTO 不下沉到 domain
- DO 不上浮到 application/domain
- MQ DTO 不在 domain 内直接流转

事件边界：
- `DomainEvent`：业务事实（语义）
- `MQ DTO`：集成契约（传输）
- 转换位置：`ddd-application/src/main/java/com/chaosopen/ddd/application/event/handler/*`

---

## 4. 模块代码模板（按此风格生成）

### 4.1 Adapter 模板

职责：只做协议适配，不写业务规则。

最小模板：
- 接收 `Cmd/Qry`
- 调用 `ApplicationServiceI`
- 返回 `Result/CO`

禁止：
- 不允许在 Controller 里做库存校验、状态流转、金额计算。

### 4.2 Application 模板

职责：用例编排 + 事务边界。

最小模板：
- `CmdExe/QryExe` 接收 DTO
- converter 转领域对象
- 编排多个 domain service
- 返回 CO

禁止：
- 不允许在 CmdExe 手工计算 `totalQuantity/totalAmount`
- 不允许直接调用 Mapper/RabbitTemplate/RemoteClient

### 4.3 Domain 模板

职责：业务规则与领域语义。

最小模板：
- `model` 提供行为方法（如 `submit/pay/cancel/totalQuantity`）
- `service` 组织本领域内行为
- `gateway` 只定义抽象接口
- 必要时发布 `DomainEvent`

禁止：
- 不允许出现 `*DTO/*DO/Mapper/RabbitTemplate`
- 不允许依赖基础设施实现类

### 4.4 Infrastructure 模板

职责：技术实现与模型映射。

最小模板：
- `gatewayImpl` 实现 domain gateway
- `converter/mapper` 完成 `Domain <-> DO`
- MQ/远程调用只在此层

禁止：
- 不允许新增业务流程编排
- 不允许把业务规则写到 gatewayImpl

---

## 5. 反模式拦截（发现即修）

1. Application 出现业务规则
- 现象：`CmdExe` 中出现金额计算、状态机判断、库存扣减算法
- 修复：下沉到 `domain model/service`

2. Domain 污染技术细节
- 现象：`domain` 里引用 `Mapper/DO/RabbitTemplate`
- 修复：抽象 `gateway`，把实现移到 `infrastructure`

3. Adapter 过载
- 现象：Controller/Consumer 编排多个领域并处理事务
- 修复：迁移到 `application`

4. 事件混层
- 现象：在 `domain` 直接创建并发送 `MQ DTO`
- 修复：domain 只发 `DomainEvent`，application handler 转 MQ DTO

5. 参数贫血
- 现象：领域核心方法只收裸 `Long/String/Map`
- 修复：改为领域实体/值对象输入，提升语义

---

## 6. 下单场景专项约束（本项目基线）

必须遵守：
- `CreateOrderCmdExe` 负责编排顺序，不做订单事实计算
- `OrderDomainService` 不跨域主动查商品/库存/用户
- `Order` 聚合根负责 `totalQuantity/totalAmount/状态流转`
- `DomainEventPublisher` 由 infrastructure 实现事务后发布

正例：
- `CreateOrderCmdExe.execute -> OrderDomainService.placeOrder`
- `Order.totalQuantity()`

反例：
- 在 `CmdExe` 里循环累加件数并写回订单
- 在 `OrderDomainServiceImpl` 注入 `SkuGateway/InventoryGateway/UserGateway` 做跨域查询

---

## 7. 执行输出模板（每次改动后强制输出）

1. 模块链路
- `adapter -> application -> domain -> infrastructure`
- 本次实际链路：`[按改动填写]`

2. 对象链路
- `Cmd/Qry -> Domain Model -> DO/MQ DTO -> CO`
- 本次对象流转：`[按改动填写]`

3. 文件归属
- `[文件绝对路径]` 属于 `[模块]`，职责 `[编排/规则/契约/技术实现]`

4. 污染检查
- `domain` 是否引用 DTO/DO/技术类：`是/否`
- `application` 是否承载业务规则：`是/否`
- `adapter` 是否承载编排逻辑：`是/否`
- 若有污染，修复点：`[具体文件与动作]`

5. 事件检查（若涉及事件）
- 领域事件：`[类名]`
- 集成消息：`[MQ DTO 类名]`
- 转换位置：`[application/event/handler/*]`

---

## 8. 完成定义（Definition of Done）

以下全部满足才算完成：
- 编译通过（至少模块级编译）
- 无新增跨层依赖
- 新增逻辑能在分层上解释清楚“为什么放这里”
- 输出第 7 节完整检查结果

---

## 9. Few-shot 示例库（生成代码前先参考）

### 9.1 Adapter 正例（只做协议适配）

```java
@RestController
public class OrderController {
    @Autowired
    private OrderServiceI orderService;

    @PostMapping("/orders")
    public Result<CreateOrderCO> create(@RequestBody CreateOrderCmd cmd) {
        return Result.success(orderService.createOrder(cmd));
    }
}
```

要点：
- 只接收 DTO、调用应用服务、返回 CO。
- 不出现库存校验/金额计算/状态流转。

### 9.2 Adapter 反例（禁止）

```java
// 反例：Controller 里直接计算总金额并判断库存
BigDecimal total = items.stream().map(...).reduce(...);
if (stock < need) { throw ...; }
```

问题：
- 把领域规则放到了 adapter，破坏边界。

### 9.3 Application 正例（编排 + 事务）

```java
@Transactional(rollbackFor = Exception.class)
public CreateOrderCO execute(CreateOrderCmd cmd) {
    List<OrderItem> items = OrderApplicationConverter.toOrderItems(cmd);
    User user = userDomainService.getByUserId(cmd.getUserId());
    Store store = buildStore(cmd.getStoreId());
    ProductOrderData data = productDomainService.validateSkusAndCollectOrderData(items);
    inventoryDomainService.deductAndSaveBatch(store, items);
    CreateOrderResult result = orderDomainService.placeOrder(items, user, store, data.getSkuMap());
    return OrderApplicationConverter.toCreateOrderCO(result.getOrder());
}
```

要点：
- 有编排顺序、有事务边界。
- 不直接依赖 Mapper/RabbitTemplate。

### 9.4 Application 反例（禁止）

```java
// 反例：CmdExe 里手工计算 totalQuantity
int totalQuantity = 0;
for (OrderItem i : items) {
    totalQuantity += i.getQuantity();
}
```

问题：
- 业务事实应由聚合根提供，如 `Order.totalQuantity()`。

### 9.5 Domain 正例（规则内聚）

```java
public void submit() {
    ensureStatus(OrderStatus.INIT);
    if (orderItems.isEmpty()) {
        throw new BizException(...);
    }
    this.status = OrderStatus.PENDING_PAYMENT;
}
```

要点：
- 状态流转和业务约束在聚合根内部完成。

### 9.6 Domain 反例（禁止）

```java
// 反例：domain 里直接发 MQ
rabbitTemplate.convertAndSend(...);
```

问题：
- domain 污染技术实现，应通过 gateway 抽象由 infrastructure 实现。

### 9.7 Infrastructure 正例（实现端口）

```java
@Component
public class SmsGatewayImpl implements SmsGateway {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void publishSms(SmsModel message) {
        rabbitTemplate.convertAndSend(...);
    }
}
```

要点：
- 技术细节留在 infrastructure。
- 不在此层新增业务规则。
