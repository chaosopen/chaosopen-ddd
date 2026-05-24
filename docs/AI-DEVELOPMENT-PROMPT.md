# chaosopen-ddd AI 开发提示词（架构链路版）

你是本项目 AI 开发助手。只关注三件事：
1. 模块调用链路
2. 对象传输链路
3. 每个模块的专注对象

不展开具体业务规则，不写具体业务场景。

---

## 1. 模块调用链路

标准调用方向：
- `adapter -> application -> domain`
- `infrastructure -> domain`（实现 domain 抽象）

说明：
- `adapter` 只作为输入/输出边界。
- `application` 只做用例编排与事务边界。
- `domain` 定义业务模型、领域行为、抽象端口。
- `infrastructure` 实现端口并承载技术细节。

禁止反向依赖：
- `domain` 不依赖 `adapter/infrastructure`。
- `adapter` 不依赖 `infrastructure` 内部实现细节。

---

## 2. 对象传输链路

对象分层流转：
1. 外部请求对象：`client/dto/*Cmd|*Qry`
2. 应用层转换：`application/converter/*`
3. 领域对象：`domain/*/model`
4. 持久化对象：`infrastructure/*/persistence/dataobject/*DO`
5. 消息对象：`client/dto/mq/*`（仅传输）

链路规则：
- `DTO/CO` 用于跨层传输，不承载领域行为。
- `Domain Model` 用于领域表达，不直接暴露给外部。
- `DO` 只用于存储映射，不上浮到 domain/application。
- `MQ DTO` 与 `Domain Model` 必须通过转换隔离。

---

## 3. 每个模块的专注对象

### `ddd-adapter`
专注对象：
- HTTP 请求/响应模型
- MQ 消息接入模型

不持有：
- 领域规则对象
- 持久化对象

### `ddd-application`
专注对象：
- 用例执行对象（CmdExe/QryExe）
- 跨层转换对象（converter）
- 事务与编排对象

不持有：
- 存储实现细节对象

### `ddd-domain`
专注对象：
- 领域模型（`*Model`）
- 领域事件（`*DomainEvent`）
- 领域服务（`*DomainService`）
- 领域依赖抽象（`*Gateway`）

不持有：
- 传输 DTO
- 存储 DO
- 技术客户端对象

### `ddd-infrastructure`
专注对象：
- 网关实现（`*GatewayImpl`）
- 持久化对象（`*DO`、`*Mapper`）
- 缓存对象、远程调用对象、MQ 发送对象

不持有：
- 业务流程编排对象

### `ddd-client`
专注对象：
- 对外契约对象（`api`、`dto`、`co`、`mq dto`）

### `ddd-common`
专注对象：
- 跨模块常量、错误码、异常、工具

---

## 4. 开发输出要求

每次任务输出必须包含：
1. 变更涉及的模块链路（从入口到落地）。
2. 新增/修改对象的传输链路。
3. 每个改动文件属于哪个模块、承载哪类对象。
4. 是否发生跨层污染（若有，指出并修复）。

