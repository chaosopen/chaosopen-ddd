# chaosopen-ddd-dev

用于 `chaosopen-ddd` 项目的架构链路规范技能。

目标：保证 AI 开发时只按“模块调用链路 + 对象传输链路 + 模块专注对象”做实现，避免分层污染。

---

## A. 模块调用链路

固定链路：
- `adapter -> application -> domain`
- `infrastructure -> domain`

边界要求：
- `domain` 不依赖 `adapter/infrastructure`。
- `adapter` 不直接依赖基础设施实现细节。
- `application` 只编排，不做领域业务计算。

下单专项约束：
- `OrderDomainService` 不跨域主动查询（不直接查商品/库存/用户网关）。
- 应用层先准备 `User/Store/Sku` 等领域实体，再调用订单领域服务。
- 订单业务事实计算（如总件数）必须放在 `Order` 聚合根方法中。
- 领域核心方法优先传领域实体/值对象，不传裸 `Long userId/storeId`。

---

## B. 对象传输链路

标准对象：
- 传输对象：`client/dto`（Cmd/Qry/CO/MQ DTO）
- 领域对象：`domain/*/model`
- 持久化对象：`infrastructure/*/persistence/dataobject/*DO`

转换位置：
- `application/converter`：DTO/CO <-> Domain
- `infrastructure/*`：Domain <-> DO/MQ DTO/Remote DTO

约束：
- DTO 不下沉 domain。
- DO 不上浮 application/domain。
- MQ DTO 与 domain model 必须隔离。

---

## C. 模块专注对象

- adapter：协议对象（HTTP/MQ）
- application：编排对象（CmdExe/QryExe/Converter）
- domain：语义对象（Model/Event/Service/Gateway 抽象，聚合根承载业务计算）
- infrastructure：实现对象（GatewayImpl/Mapper/DO/Cache/Remote）
- client：契约对象（API/DTO/CO/MQ DTO）
- common：公共对象（常量/异常/工具）

---

## D. 执行检查

每次改动后必须给出：
1. 模块调用链路图（文字即可）。
2. 对象流转路径（入参 -> 领域 -> 持久化/消息）。
3. 模块对象归属检查。
4. 跨层污染检查结论。
