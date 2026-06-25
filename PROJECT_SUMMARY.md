# 开源会计系统 - 项目总结

## 项目概述

开源会计系统是一个基于Spring Boot 3.5.10 + Java 21的企业级会计系统，采用微服务架构设计，涵盖了总账核算、报表管理、预算管理、资产管理、工作流审批等核心功能模块。

## 技术栈

### 核心框架
- **Spring Boot**: 3.5.10
- **Java**: 21
- **MySQL**: 8.4.0
- **MyBatis Plus**: 3.5.7

### 安全认证
- **Spring Security**: 安全框架
- **JWT**: Token认证
- **Spring Authorization Server**: 授权服务器

### 工作流引擎
- **Flowable**: 7.0.1
- **BPMN 2.0**: 流程定义标准

### API文档
- **SpringDoc OpenAPI**: 2.3.0
- **Swagger UI**: 交互式API文档

### 工具库
- **Lombok**: 1.18.42
- **Hutool**: 5.8.23
- **Fastjson2**: 2.0.60
- **Apache Commons**: Lang3, IO, Collections4

## 模块架构

### 1. openaccounting-common (公共模块)
**功能**:
- 统一响应封装
- 全局异常处理
- 基础实体类
- 工具类库
- 安全配置
- OpenAPI配置
- MyBatis Plus配置
- 日志拦截器和切面

**核心类**:
- `Result`: 统一响应封装
- `GlobalExceptionHandler`: 全局异常处理
- `BaseEntity`: 基础实体
- `SecurityConfig`: Spring Security配置
- `OpenApiConfig`: API文档配置

### 2. openaccounting-system (系统管理模块)
**功能**:
- 用户管理
- 角色管理
- 部门管理
- 公司管理
- 权限管理
- 用户认证授权
- Token管理

**核心接口**:
- POST /api/auth/login - 用户登录
- POST /api/auth/refresh - 刷新Token
- POST /api/auth/change-password - 修改密码
- CRUD: 用户、角色、部门、公司、权限

### 3. openaccounting-ledger (总账模块)
**功能**:
- 科目管理
- 凭证管理
- 会计期间管理
- 账簿查询
- 辅助核算
- 期末处理

**核心接口**:
- 科目管理: 增删改查、科目树
- 凭证管理: 录入、审核、过账、反过账
- 期间管理: 开启、关闭、反结账
- 账簿查询: 总账、明细账、科目余额表
- 辅助核算: 客户、供应商、项目分析
- 期末处理: 试算平衡、损益结转

### 4. openaccounting-report (报表模块)
**功能**:
- 资产负债表
- 利润表
- 现金流量表
- Excel导出

**核心接口**:
- POST /api/reports/generate - 生成报表
- GET /api/reports/export/{reportType} - 导出Excel

### 5. openaccounting-budget (预算管理模块)
**功能**:
- 预算编制
- 预算审批
- 预算监控
- 预算分析

**核心接口**:
- POST /api/budgets - 创建预算
- PUT /api/budgets/{id}/submit - 提交预算
- PUT /api/budgets/{id}/approve - 审批预算
- PUT /api/budgets/{id}/activate - 激活预算
- POST /api/budgets/monitor - 预算监控
- POST /api/budgets/analyze - 预算分析

### 6. openaccounting-asset (资产管理模块)
**功能**:
- 固定资产台账
- 资产折旧计算
- 资产盘点
- 资产处置
- 资产报表

**折旧方法**:
- 直线法
- 双倍余额递减法
- 年数总和法

**核心接口**:
- POST /api/assets - 创建资产
- POST /api/assets/{id}/depreciation - 计提折旧
- POST /api/assets/batch-depreciation - 批量计提折旧
- POST /api/assets/{id}/dispose - 资产处置
- POST /api/inventories - 创建盘点单
- POST /api/asset-reports - 生成资产报表

### 7. openaccounting-workflow (工作流模块)
**功能**:
- 流程定义管理
- 流程实例管理
- 任务管理
- Flowable集成
- 流程图生成

**核心接口**:
- POST /api/workflow/start - 启动流程
- POST /api/workflow/complete-task - 完成任务
- GET /api/workflow/my-tasks - 查询待办任务
- GET /api/workflow/instances/{id} - 查询流程实例
- GET /api/flowable/diagram/{id} - 获取流程图

## 数据库设计

### 核心表结构
- 系统管理: sys_user, sys_role, sys_permission, sys_department, sys_company
- 总账模块: gl_account, gl_voucher, gl_voucher_detail, gl_accounting_period, gl_balance
- 辅助核算: ba_customer, ba_supplier, ba_project, ba_employee
- 预算管理: bg_budget, bg_budget_detail
- 资产管理: fa_asset, fa_depreciation, fa_inventory, fa_inventory_detail
- 工作流: wf_process_definition, wf_process_instance, wf_task

## 部署架构

### 开发环境
- 本地开发，各模块独立运行
- 端口分配: 8080-8084
- 数据库: 本地MySQL

### 生产环境
- Docker容器化部署
- MySQL主从复制
- Nginx反向代理
- 负载均衡

### 访问地址
- Swagger文档: http://localhost:8080/swagger-ui.html
- 总账模块: http://localhost:8080
- 报表模块: http://localhost:8081
- 预算管理: http://localhost:8082
- 资产管理: http://localhost:8083
- 工作流模块: http://localhost:8084

## 项目特色

### 1. 模块化设计
- 清晰的模块划分
- 低耦合、高内聚
- 易于扩展和维护

### 2. 规范的代码结构
- 统一的分层架构
- 完善的异常处理
- 全面的日志记录

### 3. 完善的文档
- 安装部署指南
- 开发者指南
- API文档
- 数据库脚本

### 4. 企业级功能
- 工作流审批
- 权限管理
- 数据安全
- 审计日志

### 5. 多种折旧方法
- 直线法
- 双倍余额递减法
- 年数总和法

### 6. 完整的预算管理
- 预算编制
- 预算审批
- 预算执行监控
- 预算分析

## 开发规范

### 包命名
```
com.qoobot.openaccounting.{module}
├── controller   # 控制器层
├── service      # 服务层
├── mapper       # 数据访问层
├── entity       # 实体层
├── dto          # 数据传输对象
├── vo           # 视图对象
└── config       # 配置类
```

### API规范
- RESTful风格
- 统一响应格式
- 完善的参数校验
- 详细的API文档

### Git规范
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式
- refactor: 重构
- test: 测试
- chore: 构建/工具

## 测试

### 单元测试
- JUnit 5
- Mockito
- 测试覆盖率要求: 60%+

### 集成测试
- Spring Boot Test
- Testcontainers

## 性能优化

### 数据库优化
- 合理的索引设计
- 分页查询
- 批量操作

### 缓存策略
- Redis缓存（待实现）
- 本地缓存

### 异步处理
- Spring异步任务
- 消息队列（待实现）

## 安全措施

### 认证授权
- JWT Token
- Spring Security
- 权限控制

### 数据安全
- SQL注入防护
- XSS防护
- CSRF防护
- 敏感数据加密

### 审计日志
- 操作日志
- 登录日志
- 异常日志

## 项目进度

### 总体进度: 95%

**已完成**:
- ✅ 需求分析与设计
- ✅ 公共模块与系统管理
- ✅ 系统认证授权
- ✅ 总账核算核心
- ✅ 扩展业务模块
- ✅ 工具类完善
- ✅ 配置类完善
- ✅ 文档完善

**待完善**:
- ⏳ 单元测试
- ⏳ 集成测试
- ⏳ 性能优化
- ⏳ 安全加固
- ⏳ 上线部署

## 未来规划

### 短期目标
1. 完善单元测试和集成测试
2. 性能优化和压力测试
3. 安全加固和漏洞扫描
4. 生产环境部署

### 中期目标
1. 前端界面开发
2. 移动端适配
3. 多租户支持
4. 国际化支持

### 长期目标
1. SaaS化改造
2. AI智能分析
3. 区块链审计
4. 云原生改造

## 贡献指南

欢迎提交Issue和Pull Request！

### 提交规范
- 遵循Git提交规范
- 添加单元测试
- 更新文档
- 代码审查

### 联系方式
- 邮箱: dev@qoobot.com
- Issues: https://github.com/qoobot-com/openaccounting/issues

## 许可证

MIT License

## 致谢

感谢所有为项目做出贡献的开发者！

---

**项目地址**: https://github.com/qoobot-com/openaccounting
**文档地址**: https://github.com/qoobot-com/openaccounting/wiki
**API文档**: http://localhost:8080/swagger-ui.html

---

**最后更新**: 2026年2月15日
