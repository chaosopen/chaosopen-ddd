# chaosopen-ddd

一个基于 DDD（Domain-Driven Design）的多模块示例工程，强调：
- 业务规则下沉到 Domain
- 应用层只做用例编排
- 基础设施层只做技术实现

---

## 为什么要用 DDD

1. AI 时代代码量会爆发增长  
- 需求迭代速度更快、代码生成更频繁，项目代码体量会快速膨胀。  
- 如果缺少清晰边界，代码很容易演化为“屎山”，维护成本持续上升。  

2. 传统三层架构容易让 Service 臃肿  
- Controller-Service-DAO 模式下，业务规则、流程编排、技术细节常常混在 Service。  
- 使用 DDD 可以把“业务规则”集中到 Domain，把“用例流程”留在 Application，把“技术实现”放在 Infrastructure。  
- 这种分层方式更利于 AI 和研发协作开发：任务边界清晰、改动影响面可控、功能迭代更稳定。  

---

## DDD 的主要好处

1. 业务规则集中，减少重复实现  
- 同一规则只在 Domain 维护一份，避免在多个 Service/Controller 重复复制。  

2. 职责边界清晰，降低协作冲突  
- 接入、编排、规则、技术实现各层分工明确，团队并行开发更顺畅。  

3. 可测试性更好  
- 领域层可脱离数据库和外部依赖做单元测试，问题定位更快。  

4. 变更影响面更可控  
- 需求变化主要改 Domain 和少量编排，不会牵一发而动全身。  

5. 技术替换成本更低  
- 数据库、缓存、MQ、远程 SDK 变化通常只影响 Infrastructure 实现。  

6. 更适合复杂业务演进  
- 当规则越来越多时，领域模型能保持业务语义和结构稳定。  

7. 更适合 AI 协同开发  
- 分层边界天然适合任务拆分，AI 生成代码更容易“放对层、改对点”。  

8. 便于治理与审计  
- 关键决策和规则在 Domain 可追踪，代码审查和架构治理更清晰。  

9. 提升长期维护效率  
- 项目规模扩大后仍能保持可读、可改、可扩展，降低长期维护成本。  

---

## 模块职责

### `ddd-start`
- 启动模块。
- Spring Boot 启动类与全局配置加载。

### `ddd-adapter`
- 接入层（外部入口）。
- 提供 HTTP Controller、MQ Consumer（可扩展 Job）。
- 负责协议适配，不写核心业务规则。

### `ddd-application`
- 应用层（用例编排层）。
- 组织一次业务用例流程：参数转换、调用领域服务、返回结果。
- 处理应用级事件监听与任务衔接。

### `ddd-domain`
- 领域层（业务核心）。
- 放实体/聚合、领域服务、领域规则、领域事件、网关抽象接口。
- 不依赖数据库、缓存、MQ 客户端等技术细节。

### `ddd-infrastructure`
- 基础设施层（技术实现层）。
- 实现 domain 的 gateway 接口。
- 对接 MyBatis-Plus、Redis、RabbitMQ、第三方远程服务。

### `ddd-client`
- 契约层。
- 放 `api`、`DTO`、`Cmd/Qry`、`CO`、`MQ DTO` 等跨层传输模型。

### `ddd-common`
- 公共能力模块。
- 放通用异常、错误码、常量、工具类。

---

## 包划分（当前约定）

```text
com.chaosopen.ddd
├── adapter                    接入层：HTTP/MQ 入口适配
│   ├── config                 接入层配置
│   ├── controller             HTTP 接口
│   └── mq/consumer            MQ 消费监听
├── application                应用层：用例编排
│   ├── command                CmdExe/QryExe
│   ├── converter              应用层对象转换
│   ├── event/handler          应用事件处理器
│   ├── config                 应用层装配配置
│   └── service                应用服务实现
├── client                     契约层
│   ├── api                    服务接口
│   └── dto                    DTO/CO/MQ消息
├── common                     通用组件
├── domain                     领域层：业务模型与规则
│   ├── event                  领域事件发布抽象
│   ├── order                  订单领域（model/service/gateway/event/enums）
│   ├── product                商品领域（model/gateway/enums）
│   ├── inventory              库存领域（model/gateway）
│   ├── user                   用户领域（model/gateway）
│   └── sms                    短信领域（model/gateway）
├── infrastructure             基础设施实现
│   ├── common/event           领域事件发布实现
│   ├── config                 MyBatis/RabbitMQ 等配置
│   ├── order|product|inventory|user
│   │   ├── gatewayImpl        网关实现
│   │   └── persistence        mapper + dataobject
│   └── sms
│       ├── gatewayImpl        短信网关实现
│       └── remote             第三方短信客户端
└── start                      启动层
```

---

## 开发任务边界（谁做什么）

### 1. Adapter（接入层）
可以做：
- 请求接收、基础参数校验、响应封装。
- MQ 消息接入与模型转换。

不应该做：
- 库存判断、状态流转、积分计算等业务规则。

### 2. Application（应用层）
可以做：
- 用例编排（调用多个领域能力）。
- 事务边界控制。
- 应用事件处理与后续动作衔接。

不应该做：
- 持久化细节（SQL/Mapper 细节）。
- 复杂业务规则实现（应下沉 Domain）。

### 3. Domain（领域层）
可以做：
- 实体行为、领域规则、跨聚合规则编排。
- 领域事件定义与发布抽象。

不应该做：
- 直接依赖 `Mapper/RedisTemplate/RabbitTemplate/HTTP Client`。

### 4. Infrastructure（基础设施层）
可以做：
- 实现 domain gateway。
- DTO/DO/缓存模型与领域模型转换。
- MQ 发送、远程调用、ORM 映射。

不应该做：
- 定义业务流程、抢占领域规则职责。

---

## 依赖方向（必须遵守）

- `adapter -> application -> domain`
- `infrastructure -> domain`（实现 domain 抽象）
- `client` 作为契约层被 adapter/application 复用
- `domain` 不依赖 `infrastructure/adapter`

---

## 下单链路（当前实现）

1. Adapter 接收下单请求。
2. Application `CreateOrderCmdExe` 编排下单用例（事务边界）。
3. Domain `OrderDomainService` 执行业务规则：
- 商品上架校验
- 库存校验与扣减
- 商品销量增加
- 用户积分增加
- 订单持久化相关变更
4. Domain 发布 `OrderCreatedDomainEvent`（通过 `DomainEventPublisher` 抽象）。
5. Infrastructure 在事务提交后发布应用事件。
6. Application 事件处理器将事件转为短信领域模型并调用短信网关。
7. Infrastructure 短信网关发送 MQ；Adapter 消费 MQ 后触发供应商短信发送。

---

## 命名与落位建议

- 领域对象优先语义命名：如 `OrderCreatedDomainEvent`、`SmsModel`。
- MQ 传输对象与领域对象分离：`client.dto.mq.*` 仅做传输契约。
- 常量跨层复用放 `ddd-common`（例如 MQ routing 常量）。

---

## 本地运行与测试

```bash
mvn -q test
```

如需启动服务：
- 启动模块：`ddd-start`
