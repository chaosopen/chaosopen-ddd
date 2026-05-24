# chaosopen-ddd AI 开发提示词（可执行版）

你是本项目 AI 开发助手。目标不是“写能跑的代码”，而是“写出与本项目一致、长期可维护、符合 DDD 边界的代码”。

---

## 0. 执行方式（强制）

面对任何开发任务，必须按顺序执行：
1. 先做分层落位判断（逻辑放哪一层）。
2. 再画模块链路（谁调用谁）。
3. 再定对象链路（DTO/Domain/DO/MQ DTO 怎么流转）。
4. 按分层模板编码。
5. 用反模式清单做污染检查。
6. 按统一模板输出结果。

禁止跳步直接编码。

---

## 1. 分层落位判断

先判断逻辑类型，再决定文件落位：
- 协议接入（HTTP/MQ 入站、响应封装）-> `ddd-adapter`
- 用例编排（事务、调用顺序、跨领域协作）-> `ddd-application`
- 业务规则（状态流转、约束校验、金额/数量事实）-> `ddd-domain`
- 技术实现（DB/MQ/缓存/远程调用）-> `ddd-infrastructure`
- 对外契约（`Cmd/Qry/CO/MQ DTO/API`）-> `ddd-client`
- 公共能力（异常、错误码、常量、工具）-> `ddd-common`

拿不准时默认：
- “规则”进 `domain`
- “流程”进 `application`

---

## 2. 模块调用链路（硬约束）

固定调用方向：
- `adapter -> application -> domain`
- `infrastructure -> domain`（实现 domain 抽象）

禁止反向依赖：
- `domain` 不依赖 `adapter/infrastructure`
- `application` 不依赖 `infrastructure` 实现类
- `adapter` 不依赖 `infrastructure` 内部细节

项目代码对照（按此风格实现）：
- 接入入口：`ddd-adapter/src/main/java/com/chaosopen/ddd/adapter/controller/order/OrderController.java`
- 用例编排：`ddd-application/src/main/java/com/chaosopen/ddd/application/command/order/CreateOrderCmdExe.java`
- 聚合规则：`ddd-domain/src/main/java/com/chaosopen/ddd/domain/order/model/Order.java`
- 领域服务：`ddd-domain/src/main/java/com/chaosopen/ddd/domain/order/service/impl/OrderDomainServiceImpl.java`
- 事件发布实现：`ddd-infrastructure/src/main/java/com/chaosopen/ddd/infrastructure/common/event/DomainEventPublisherImpl.java`

---

## 3. 对象传输链路（硬约束）

对象类型：
1. 外部契约：`ddd-client` 下 `Cmd/Qry/CO/MQ DTO`
2. 领域语义：`ddd-domain` 下 `Model/Event/Service/Gateway`
3. 存储对象：`ddd-infrastructure` 下 `DO/Mapper`

转换位置：
- `application/converter`：`DTO/CO <-> Domain`
- `infrastructure/*`：`Domain <-> DO/MQ DTO/Remote DTO`

禁止：
- DTO 下沉到 domain
- DO 上浮到 application/domain
- MQ DTO 在 domain 内直接流转

事件边界：
- `DomainEvent`：表达业务事实（语义优先）
- `MQ DTO`：表达集成契约（传输优先）
- DomainEvent -> MQ DTO 转换在 `ddd-application/.../event/handler/*`

---

## 4. 模块职责模板（编码时必须遵守）

### 4.1 `ddd-adapter`
可以做：
- 参数接收、基础校验、响应封装
- MQ 入站消息适配

不可以做：
- 库存判断、金额计算、状态流转、跨领域流程编排

### 4.2 `ddd-application`
可以做：
- `CmdExe/QryExe` 用例编排
- 事务边界（如 `@Transactional`）
- 调用多个领域服务并组装返回

不可以做：
- 业务事实计算（如 `totalQuantity/totalAmount`）
- 直接写 SQL / 直接调 MQ 客户端 / 直接调远程 SDK

### 4.3 `ddd-domain`
可以做：
- 聚合根行为与业务规则
- 领域服务
- 领域事件定义与发布抽象
- Gateway 抽象定义

不可以做：
- 引用 DTO/DO/Mapper/RabbitTemplate/RemoteClient
- 依赖基础设施实现类

### 4.4 `ddd-infrastructure`
可以做：
- Gateway 实现
- Domain 与 DO/MQ DTO/Remote DTO 映射
- DB/MQ/缓存/远程调用技术细节

不可以做：
- 业务流程编排
- 领域核心规则定义

---

## 5. 下单场景专项约束（项目基线）

必须遵守：
- `CreateOrderCmdExe` 只负责编排顺序，不做订单事实计算
- `OrderDomainService` 不跨域主动查商品/库存/用户
- `Order` 聚合根负责 `totalQuantity/totalAmount/状态流转`
- `DomainEventPublisher` 由 infrastructure 实现事务后发布

正例：
- `CreateOrderCmdExe.execute -> OrderDomainService.placeOrder`
- `Order.totalQuantity()`

反例：
- 在 `CmdExe` 内手工累加数量/金额并回写订单
- 在 `OrderDomainServiceImpl` 注入商品/库存/用户网关做跨域查询

---

## 6. Few-shot 正反例（生成前参考）

### 6.1 Adapter 正例
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

### 6.2 Application 正例
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

### 6.3 Domain 正例
```java
public void submit() {
    ensureStatus(OrderStatus.INIT);
    if (orderItems.isEmpty()) {
        throw new BizException(...);
    }
    this.status = OrderStatus.PENDING_PAYMENT;
}
```

### 6.4 反例（禁止）
```java
// 在 Controller/CmdExe 中做金额与库存规则
// 在 domain 中直接调用 rabbitTemplate/mapper
```

---

## 7. 反模式拦截（发现即修）

1. Application 规则上移
- 现象：`CmdExe` 里出现状态机/金额算法/库存算法
- 修复：下沉 `domain model/service`

2. Domain 技术污染
- 现象：`domain` 引用 `Mapper/DO/RabbitTemplate`
- 修复：改为 `gateway` 抽象 + `infrastructure` 实现

3. Adapter 过载
- 现象：Controller/Consumer 内做跨领域编排和事务
- 修复：迁移到 `application`

4. 事件混层
- 现象：domain 里直接发送 MQ DTO
- 修复：domain 发 DomainEvent，application handler 转 MQ DTO

---

## 8. 输出模板（每次任务都必须给）

1. 模块链路：
- `adapter -> application -> domain -> infrastructure`
- 本次实际链路：`[...]`

2. 对象链路：
- `Cmd/Qry -> Domain Model -> DO/MQ DTO -> CO`
- 本次对象流转：`[...]`

3. 文件归属：
- `[文件路径]` 属于 `[模块]`，职责 `[编排/规则/契约/技术实现]`

4. 污染检查：
- `domain` 是否引用 DTO/DO/技术类：`是/否`
- `application` 是否承载业务规则：`是/否`
- `adapter` 是否承载编排逻辑：`是/否`
- 若有污染，修复动作：`[...]`

5. 事件检查（涉及事件时）：
- 领域事件：`[...]`
- 集成消息：`[...]`
- 转换位置：`[...]`

---

## 9. 完成定义（DoD）

以下全部满足才算完成：
- 编译通过（至少模块级）
- 无新增跨层依赖
- 规则与流程边界清晰
- 已按第 8 节输出完整检查结果

