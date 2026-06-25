# OpenAccounting

<div align="center">

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-green.svg)

**一款基于 Spring Boot 3.5.10 + Java 21 的企业级开源会计系统**

[功能特性](#功能特性) • [快速开始](#快速开始) • [技术文档](#技术文档) • [贡献指南](#贡献指南)

</div>

---

## 项目简介

OpenAccounting 是一个功能完善的企业级会计管理系统，采用微服务架构设计，涵盖总账核算、报表管理、预算管理、资产管理、工作流审批等核心功能模块。系统基于 Spring Boot 3.5.10 和 Java 21 构建，整合了 MyBatis Plus、Spring Security、Flowable 等主流技术栈，提供完善的权限管理、审批流程和数据安全保障。

## 核心功能

### 🔐 系统管理
- 用户管理、角色管理、部门管理、公司管理
- 权限管理与 RBAC 模型
- JWT 认证与授权
- 审计日志记录

### 📊 总账核算
- 科目管理与科目树
- 凭证录入、审核、过账
- 会计期间管理
- 账簿查询（总账、明细账、科目余额表）
- 辅助核算（客户、供应商、项目）
- 期末处理（试算平衡、损益结转）

### 📈 报表管理
- 资产负债表
- 利润表
- 现金流量表
- Excel 导出功能

### 💰 预算管理
- 预算编制与审批
- 预算执行监控
- 预算分析
- 预算预警

### 📦 资产管理
- 固定资产台账
- 多种折旧方法（直线法、双倍余额递减法、年数总和法）
- 资产盘点与处置
- 资产报表

### 🔄 工作流审批
- 基于 Flowable 的流程引擎
- 流程定义与管理
- 任务管理与流程监控
- 流程图可视化

## 技术栈

| 类别 | 技术 |
|------|------|
| 核心框架 | Spring Boot 3.5.10, Java 21 |
| 数据库 | MySQL 8.4.0 |
| ORM | MyBatis Plus 3.5.7 |
| 安全认证 | Spring Security, JWT |
| 工作流 | Flowable 7.0.1 |
| API文档 | SpringDoc OpenAPI 2.3.0 |
| 工具库 | Lombok, Hutool, Fastjson2, Apache Commons |

## 模块架构

```
openaccounting
├── openaccounting-common      # 公共模块
├── openaccounting-system      # 系统管理模块
├── openaccounting-ledger      # 总账核算模块
├── openaccounting-report      # 报表管理模块
├── openaccounting-budget      # 预算管理模块
├── openaccounting-asset       # 资产管理模块
└── openaccounting-workflow    # 工作流模块
```

详细架构说明请查看 [PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md)

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.4.0+
- Docker (可选)

### 安装部署

#### 方式一：Docker 部署（推荐）

```bash
# 克隆项目
git clone https://github.com/qoobot-com/openaccounting.git
cd openaccounting

# 启动服务
docker-compose up -d

# 查看日志
docker-compose logs -f
```

#### 方式二：本地运行

```bash
# 1. 导入数据库脚本
mysql -u root -p < docs/database/openaccounting.sql

# 2. 修改数据库配置
# 编辑各模块的 application.yml 文件，配置数据库连接信息

# 3. 编译项目
mvn clean install -DskipTests

# 4. 启动各模块
# 总账模块
java -jar openaccounting-ledger/target/openaccounting-ledger.jar

# 其他模块依次启动...
```

### 访问地址

| 服务 | 地址 |
|------|------|
| API 文档 | http://localhost:8080/swagger-ui.html |
| 总账模块 | http://localhost:8080 |
| 报表模块 | http://localhost:8081 |
| 预算管理 | http://localhost:8082 |
| 资产管理 | http://localhost:8083 |
| 工作流模块 | http://localhost:8084 |

## 项目特色

- 🏗️ **模块化设计**: 清晰的模块划分，低耦合、高内聚
- 📝 **规范代码**: 统一的分层架构，完善的异常处理
- 🛡️ **企业级安全**: JWT 认证、权限控制、审计日志
- 🚀 **高性能**: 优化的数据库设计，缓存策略，异步处理
- 📊 **完整功能**: 涵盖会计核算的核心业务场景
- 🔄 **工作流集成**: Flowable 流程引擎，灵活的审批流程

## 技术文档

- [项目总结](./PROJECT_SUMMARY.md) - 详细的架构和功能说明
- [数据库设计](./docs/database/) - 数据库表结构和初始化脚本
- [API 文档](./docs/api/) - 接口文档
- [开发指南](./docs/development/) - 开发规范和最佳实践

## 开发规范

### 包命名结构

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

### Git 提交规范

- `feat`: 新功能
- `fix`: 修复 bug
- `docs`: 文档更新
- `style`: 代码格式
- `refactor`: 重构
- `test`: 测试
- `chore`: 构建/工具

## 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 查看测试覆盖率
mvn jacoco:report
```

## 项目进度

总体进度: 95%

- ✅ 需求分析与设计
- ✅ 公共模块与系统管理
- ✅ 系统认证授权
- ✅ 总账核算核心
- ✅ 扩展业务模块
- ✅ 工具类与配置完善
- ✅ 文档与 Docker 部署
- ⏳ 单元测试与集成测试
- ⏳ 性能优化
- ⏳ 安全加固

## 未来规划

- [ ] 完善单元测试和集成测试
- [ ] 前端界面开发
- [ ] 移动端适配
- [ ] 多租户支持
- [ ] 国际化支持
- [ ] SaaS 化改造
- [ ] AI 智能分析
- [ ] 区块链审计

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'feat: 添加某个功能'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 许可证

本项目采用 [MIT License](./LICENSE) 开源协议。

## 致谢

感谢所有为项目做出贡献的开发者！

---

<div align="center">

**项目地址**: https://github.com/qoobot-com/openaccounting

**文档地址**: https://github.com/qoobot-com/openaccounting/wiki

**API 文档**: http://localhost:8080/swagger-ui.html

Made with ❤️ by Qoobot Team

</div>
