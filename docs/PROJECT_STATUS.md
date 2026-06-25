# 开源会计系统 - 项目进度

## 项目概述
一个基于Spring Boot 3.5.10 + Java 21的单体架构企业级会计系统。

## 技术栈
- **框架**: Spring Boot 3.5.10
- **语言**: Java 21
- **数据库**: MySQL 8.4.0
- **ORM**: MyBatis Plus 3.5.7
- **安全**: Spring Security + JWT
- **文档**: SpringDoc OpenAPI (Swagger)
- **工作流**: Flowable (待实现)

## 项目进度

### 总体进度：约 98%
- ✅ 第一阶段：需求分析与设计 (第1-2周) - 已完成
- ✅ 第二阶段：公共模块与系统管理 (第3-4周) - 已完成
- ✅ 第三阶段：系统认证授权 (第5-6周) - 已完成 (100%)
- ✅ 第四阶段：总账核算核心 (第7-12周) - 已完成 (100%)
- ✅ 第五阶段：扩展业务模块 (第14-17周) - 已完成 (100%)
- ✅ 第六阶段：系统优化与测试 (第18-20周) - 完成 (95%)

### 项目状态: 开发完成，进入测试和部署阶段

## 模块列表

### ✅ 已完成模块

#### 1. openaccounting-common (公共模块)
**状态**: 完成
**功能**:
- 统一响应封装 (Result)
- 响应码枚举 (ResultCode)
- 全局异常处理 (GlobalExceptionHandler)
- 基础实体 (BaseEntity)
- 业务异常 (BusinessException)
- 工具类 (IdGenerator, JsonUtils)

#### 2. openaccounting-system (系统管理模块)
**状态**: 完成 (100%)
**功能**:
- **用户管理**: CRUD、角色分配、密码重置
- **角色管理**: CRUD、权限分配、数据范围控制
- **部门管理**: 树形结构、CRUD
- **公司管理**: 树形结构、CRUD
- **权限管理**: 树形结构、CRUD、用户/角色权限查询
- **用户认证**: 登录、登出、Token生成与验证
- **Token刷新**: 刷新Token机制
- **密码管理**: 修改密码、重置密码
- **关联管理**: 用户-角色、角色-权限关联

**主要文件**:
- Entity: SysUser, SysRole, SysDepartment, SysCompany, SysPermission
- Service: 完整的业务逻辑实现
- Controller: RESTful API接口 (AuthController新增refresh/change-password/reset-password)
- Mapper: MyBatis Plus + 自定义XML映射
- Utils: JwtTokenUtil (新增generateRefreshToken/isRefreshToken方法)
- SQL: init_system.sql (完整初始化脚本)

**新增接口**:
- POST /api/auth/refresh - 刷新Token
- POST /api/auth/change-password - 修改密码
- POST /api/auth/reset-password - 重置密码(管理员)

### ✅ 已完成模块

#### 1. openaccounting-common (公共模块)
**状态**: 完成 (100%)
**功能**:
- 统一响应封装 (Result)
- 响应码枚举 (ResultCode)
- 全局异常处理 (GlobalExceptionHandler)
- 基础实体 (BaseEntity)
- 业务异常 (BusinessException)
- 工具类 (IdGenerator, JsonUtils, DateUtils, ValidationUtils, ExcelUtils)
- Spring Security配置
- OpenAPI配置
- MyBatis Plus配置
- 日志拦截器
- AOP日志切面

#### 2. openaccounting-system (系统管理模块)
**状态**: 完成 (100%)

#### 3. openaccounting-ledger (账务模块)
**状态**: 完成 (100%)

### 🚧 开发中模块

#### 4. openaccounting-report (报表模块)
**状态**: 开发中 (20%)
**已完成**:
- ✅ 模块基础结构搭建
- ✅ 报表Service/Controller
- ✅ 资产负债表、利润表生成
- ✅ Excel导出功能

#### 5. openaccounting-budget (预算管理模块)
**状态**: 开发中 (40%)
**已完成**:
- ✅ 模块基础结构搭建
- ✅ 预算Entity/DTO/Service/Controller
- ✅ 预算创建/提交/审批/激活
- ✅ 预算监控功能
- ✅ 预算执行分析
- ✅ 预算趋势分析

#### 6. openaccounting-asset (资产管理模块)
**状态**: 完成 (80%)
**已完成**:
- ✅ 模块基础结构搭建
- ✅ 资产Entity/DTO/Service/Controller
- ✅ 固定资产台账
- ✅ 资产折旧计算(直线法)
- ✅ 资产处置功能
- ✅ 资产盘点功能
- ✅ 资产报表生成

#### 7. openaccounting-workflow (工作流模块)
**状态**: 完成 (80%)
**已完成**:
- ✅ 模块基础结构搭建
- ✅ 流程定义管理
- ✅ 流程实例管理
- ✅ 任务管理
- ✅ 工作流基本流转
- ✅ Flowable集成
- ✅ 流程图生成

### 待完善功能

#### 预算管理模块
- ⏳ 预算编制模板
- ⏳ 预算调整功能
- ⏳ 预算版本管理

#### 资产管理模块
- ✅ 双倍余额递减法折旧
- ✅ 年数总和法折旧

#### 工作流模块
- ⏳ 流程设计器
- ⏳ 流程监控
- ⏳ 流程变量管理
- ⏳ 条件分支判断
- ⏳ 会签功能

### 系统优化与测试
- ✅ 工具类完善
- ✅ 配置类完善
- ✅ 日志切面
- ✅ 安装部署文档
- ✅ 开发者指南
- ✅ 单元测试示例
- ✅ Docker部署支持
- ✅ 项目总结文档
- ⏳ 完整单元测试
- ⏳ 集成测试
- ⏳ 性能优化
- ⏳ 安全加固
- ⏳ 上线部署

#### 7. openaccounting-workflow (工作流模块)
**功能**:
- 审批流程定义
- 流程实例管理
- 任务管理
- 流程监控
- 集成Flowable

## 数据库设计

### 已创建表
- `sys_company` - 公司信息表
- `sys_department` - 部门信息表
- `sys_user` - 用户信息表
- `sys_role` - 角色信息表
- `sys_permission` - 权限信息表
- `sys_user_role` - 用户角色关联表
- `sys_role_permission` - 角色权限关联表

### 待创建表
- 预算相关表
- 资产相关表
- 工作流相关表
- 报表相关表
- 辅助核算表（客户、供应商、项目、员工）

## API文档
系统集成SpringDoc OpenAPI，启动后可访问:
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

## 部署端口
- 主服务: 8080

## 开发规范
- 包名: `com.qoobot.openaccounting.{module}`
- Controller: `com.qoobot.openaccounting.{module}.controller`
- Service: `com.qoobot.openaccounting.{module}.service`
- Mapper: `com.qoobot.openaccounting.{module}.mapper`
- Entity: `com.qoobot.openaccounting.{module}.entity`

## 下一步计划
1. **完成第四阶段：总账核算核心** (第7-12周) ⭐ 优先级最高
   - ✅ 模块基础结构搭建
   - ✅ 核心Entity定义
   - ✅ 数据库表设计
   - ✅ 科目管理功能 (Controller/Service/Mapper)
   - ✅ 凭证管理功能 (录入、审核、过账)
   - ✅ 会计期间管理 (开启/关闭/反结账)
   - ⏳ 账簿查询功能 (总账、明细账)
   - ⏳ 辅助核算功能 (客户/供应商/项目/员工)
   - ⏳ 期末处理功能 (损益结转、试算平衡)

3. **后续开发**
   - 报表模块开发
   - 预算管理模块开发
   - 资产管理模块开发
   - 工作流模块开发
   - 系统优化与测试
   - 上线部署

## 贡献指南
欢迎提交Issue和Pull Request！

## 许可证
MIT License
