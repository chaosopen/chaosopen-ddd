# ddd-architecture-review

通用 DDD 架构审查与落地执行 skill，可直接用于任意 Java 分层项目（多模块或单模块）。

目标：让 AI/工程师在不同代码库中稳定识别并修复 DDD 常见问题，重点覆盖：
- 跨层依赖
- 对象污染（DTO/DO 混层）
- 职责错位（应用写规则 / 领域写集成）

---

## 0. 使用方式（强制）

当任务涉及 DDD 架构改造或代码审查时，必须按以下顺序执行：
1. 先做“分层落位判断”（见第 1 节）。
2. 再做“链路与依赖方向确认”（见第 2 节）。
3. 跑“三类审查命令”（见第 5 节）。
4. 给出“按严重度排序的问题清单”。
5. 实施修复后再次跑“三类审查命令”并对比结果。
6. 按“输出模板”汇报（见第 7 节）。

禁止跳步直接改代码。

---

## 1. 分层落位判断（先判断再编码）

先判断新增逻辑属于哪类：
- 协议接入：HTTP/MQ 入参解析、响应组装 -> `adapter/interface`
- 用例编排：事务、跨领域调用顺序、调用拼装 -> `application`
- 业务规则：状态流转、金额计算、业务约束、领域事实 -> `domain`
- 技术实现：DB/MQ/缓存/三方 SDK -> `infrastructure`
- 对外契约：`Cmd/Qry/CO/MQ DTO/API` -> `client/contracts`
- 通用能力：错误码、异常、常量、工具 -> `common`

若无法判断，优先：
- “规则”进 `domain`
- “流程”进 `application`

---

## 2. 固定链路与依赖方向

推荐调用链：
- `adapter -> application -> domain`
- `infrastructure -> domain`（实现 domain 抽象）

硬性依赖约束：
- `domain` 禁止依赖 `adapter/infrastructure`
- `application` 禁止依赖 `infrastructure` 实现细节（读模型特例除外）
- `adapter` 禁止编排领域流程和业务规则

---

## 3. 对象链路与转换位置

对象类型：
- 外部传输：`Cmd/Qry/CO/MQ DTO`
- 领域语义：`Entity/ValueObject/DomainEvent/DomainService`
- 存储映射：`DO/PO/Mapper`

转换位置建议：
- `application/converter`：`DTO/CO <-> Domain`
- `infrastructure/converter`：`Domain <-> DO/MQ DTO/Remote DTO`

强约束：
- DTO 不下沉到 domain
- DO 不上浮到 application/domain
- MQ DTO 不在 domain 内直接流转

事件边界：
- `DomainEvent`：业务事实
- `IntegrationEvent/MQ DTO`：外部契约
- 转换位置：优先在 `application` 事件处理层或 `infrastructure` 适配层

---

## 4. 模块职责模板（通用）

### 4.1 Adapter

职责：协议适配，不写业务规则。

最小模板：
- 接收输入 DTO / 消息
- 调用 application 用例
- 返回输出 DTO / ACK

禁止：
- 业务规则计算
- 跨领域编排
- 直接调用 domain gateway/infrastructure mapper

### 4.2 Application

职责：用例编排 + 事务边界。

最小模板：
- `CmdExe/QryExe/UseCase` 接收 DTO
- 调用 domain service / aggregate
- 组织跨域调用顺序
- 触发事件发布（若采用应用层发布策略）

禁止：
- 核心业务规则计算（应在 domain）
- 直接依赖技术实现类（Mapper/RabbitTemplate/SDK）

### 4.3 Domain

职责：业务规则与领域语义。

最小模板：
- aggregate/entity 承载行为和约束
- domain service 组织本域行为
- gateway 仅定义抽象端口

禁止：
- 引用 DTO/DO/Mapper/RabbitTemplate/HTTP SDK
- 写事务编排流程

### 4.4 Infrastructure

职责：技术实现与模型映射。

最小模板：
- 实现 domain/application 抽象端口
- 处理 DB/MQ/缓存/远程调用
- 完成 Domain <-> 外部模型转换

禁止：
- 新增业务规则
- 新增用例编排

---

## 5. DDD 审查三大类（执行前/提交前必跑）

### A. 跨层依赖检查

```bash
rg -n "import .*infrastructure|import .*adapter" src/main/java/*domain* -S
rg -n "import .*infrastructure" src/main/java/*application* -S
rg -n "import .*domain" src/main/java/*adapter* -S
```

判定规则：
- `domain` 依赖 `adapter/infrastructure`：不通过
- `application` 依赖 `infrastructure`：
  - 命令/编排场景：不通过
  - 查询读模型：可例外，需明确标注并给治理计划
- `adapter` 直接依赖 `domain`：不通过

### B. 对象污染检查（DTO/DO/MQ DTO）

```bash
rg -n "DTO|DO|PO|Mapper|Message" src/main/java/*domain* -S
rg -n "import .*dataobject|import .*mapper" src/main/java/*application* -S
```

判定规则：
- domain 出现 DTO/DO/Mapper/MQ DTO：不通过
- application 出现 DO/Mapper：
  - 命令场景不通过
  - 查询场景可例外（需标注）

### C. 职责错位检查

```bash
rg -n "RabbitTemplate|JdbcTemplate|RedisTemplate|Feign|RestTemplate" src/main/java/*domain* -S
rg -n "status|submit\(|pay\(|cancel\(|totalAmount|totalQuantity|库存|金额|状态机" src/main/java/*application* -S
rg -n "gateway|domain service|domain\." src/main/java/*adapter* -S
```

判定规则：
- domain 写技术集成：不通过
- application 写核心规则：不通过
- adapter 写编排/领域调用：不通过

---

## 6. 修复策略优先级

1. 先修 High（阻断提交）
2. 再修 Medium（可带例外说明）
3. 最后处理 Low（优化项）

标准修复手法：
- 跨层依赖：上移/下沉到正确层，改成抽象接口依赖
- 对象污染：引入中转模型或转换器，隔离 DTO/DO
- 职责错位：把规则下沉到 domain，把编排上移到 application

---

## 7. 执行输出模板（每次改动后强制输出）

1. 模块链路
- 目标链路：`adapter -> application -> domain -> infrastructure`
- 本次实际链路：`[填写]`

2. 对象链路
- `Input DTO -> Domain -> Integration DTO/DO -> Output DTO`
- 本次对象流转：`[填写]`

3. 发现清单（按严重度）
- High：`[文件:行号 + 问题 + 修复]`
- Medium：`[文件:行号 + 问题 + 修复/例外]`
- Low：`[文件:行号 + 优化建议]`

4. 三类审查结论
- 跨层依赖：`通过/不通过`（证据摘要）
- 对象污染：`通过/不通过`（证据摘要）
- 职责错位：`通过/不通过`（证据摘要）

5. 例外清单（若有）
- `[文件 + 原因 + 治理计划时间点]`

---

## 8. 完成定义（DoD）

以下全部满足才算完成：
- 编译/测试通过（至少模块级）
- 无新增 High 问题
- Medium 问题要么修复，要么有明确例外说明
- 已执行第 5 节三类审查并输出证据

---

## 9. 审查任务 Prompt 模板（可复用）

```text
你是 DDD 架构审查助手。请仅按以下三类给出结论：
1) 跨层依赖
2) 对象污染（DTO/DO混层）
3) 职责错位（应用写规则/领域写集成）

审查范围：
- [填目录/模块]

执行要求：
- 先运行三类审查命令（见 skill 第5节）
- 输出按 High/Medium/Low 排序
- 每条包含：文件路径、行号、违反规则、修复建议
- 对“读模型例外”必须单独标注并给治理计划
- 最后给出：
  - 模块链路结论
  - 对象链路结论
  - 是否允许合并（是/否）
```

边改边审查追加语句：

```text
改动后必须再次运行三类审查命令，并输出改动前后对比结论。
```
