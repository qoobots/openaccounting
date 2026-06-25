# 开源会计系统 - 安装部署指南

## 环境要求

- JDK 21+
- MySQL 8.4.0+
- Maven 3.9.0+

## 数据库初始化

### 1. 创建数据库

```sql
CREATE DATABASE openaccounting DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 执行初始化脚本

按照以下顺序执行SQL脚本：

1. `docs/init_system.sql` - 系统管理模块
2. `docs/init_ledger.sql` - 总账模块
3. `docs/init_budget.sql` - 预算管理模块
4. `docs/init_asset.sql` - 资产管理模块
5. `docs/init_workflow.sql` - 工作流模块

```bash
mysql -u root -p openaccounting < docs/init_system.sql
mysql -u root -p openaccounting < docs/init_ledger.sql
mysql -u root -p openaccounting < docs/init_budget.sql
mysql -u root -p openaccounting < docs/init_asset.sql
mysql -u root -p openaccounting < docs/init_workflow.sql
```

## 编译项目

```bash
cd d:/06workspaces/openaccounting
mvn clean install -DskipTests
```

## 配置修改

### 数据库配置

修改各模块的 `application.yml` 文件中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/openaccounting?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 端口配置

各模块默认端口：

- openaccounting-ledger: 8080
- openaccounting-report: 8081
- openaccounting-budget: 8082
- openaccounting-asset: 8083
- openaccounting-workflow: 8084

## 启动服务

### 方式一：IDEA启动

1. 打开IDEA，导入项目
2. 找到各模块的启动类，右键运行

### 方式二：命令行启动

```bash
# 总账模块
cd openaccounting-ledger
mvn spring-boot:run

# 报表模块
cd openaccounting-report
mvn spring-boot:run

# 预算管理模块
cd openaccounting-budget
mvn spring-boot:run

# 资产管理模块
cd openaccounting-asset
mvn spring-boot:run

# 工作流模块
cd openaccounting-workflow
mvn spring-boot:run
```

## 访问系统

### API文档

启动后访问Swagger文档：

- 总账模块: http://localhost:8080/swagger-ui.html
- 报表模块: http://localhost:8081/swagger-ui.html
- 预算管理模块: http://localhost:8082/swagger-ui.html
- 资产管理模块: http://localhost:8083/swagger-ui.html
- 工作流模块: http://localhost:8084/swagger-ui.html

### 默认账号

- 用户名: admin
- 密码: admin123

## 生产环境部署

### 打包

```bash
mvn clean package -DskipTests
```

### 运行JAR包

```bash
java -jar openaccounting-ledger/target/openaccounting-ledger-1.0.0-SNAPSHOT.jar
java -jar openaccounting-report/target/openaccounting-report-1.0.0-SNAPSHOT.jar
java -jar openaccounting-budget/target/openaccounting-budget-1.0.0-SNAPSHOT.jar
java -jar openaccounting-asset/target/openaccounting-asset-1.0.0-SNAPSHOT.jar
java -jar openaccounting-workflow/target/openaccounting-workflow-1.0.0-SNAPSHOT.jar
```

## Docker部署（待完善）

TODO: 添加Docker部署方式

## 常见问题

### 数据库连接失败

1. 检查MySQL服务是否启动
2. 检查数据库用户名密码是否正确
3. 检查数据库是否已创建

### 端口冲突

修改 `application.yml` 中的 `server.port` 配置

### Maven编译失败

1. 检查JDK版本是否为21+
2. 检查Maven版本是否为3.9.0+
3. 检查网络连接，尝试使用国内Maven镜像

## 技术支持

如有问题，请提交Issue或联系：dev@qoobot.com
