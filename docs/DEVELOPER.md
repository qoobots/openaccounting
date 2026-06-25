# 开源会计系统 - 开发者指南

## 项目结构

```
openaccounting/
├── openaccounting-common/    # 公共模块
├── openaccounting-system/    # 系统管理模块
├── openaccounting-ledger/    # 总账模块
├── openaccounting-report/    # 报表模块
├── openaccounting-budget/    # 预算管理模块
├── openaccounting-asset/     # 资产管理模块
├── openaccounting-workflow/  # 工作流模块
└── docs/                     # 文档
```

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
├── config       # 配置类
└── utils        # 工具类
```

### Controller规范

```java
@Tag(name = "模块名称", description = "模块描述")
@RestController
@RequestMapping("/api/xxx")
@RequiredArgsConstructor
public class XxxController {

    private final XxxService xxxService;

    @Operation(summary = "接口说明")
    @PostMapping
    public Result<ReturnType> method(@Valid @RequestBody RequestType request) {
        return Result.success(xxxService.method(request));
    }
}
```

### Service规范

```java
public interface XxxService extends IService<XxxEntity> {
    // 方法签名
}

@Service
@RequiredArgsConstructor
public class XxxServiceImpl extends ServiceImpl<XxxMapper, XxxEntity> implements XxxService {
    // 方法实现
}
```

### Entity规范

```java
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("table_name")
@Schema(description = "实体描述")
public class XxxEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键")
    private Long id;
    // 其他字段
}
```

## 数据库规范

### 表命名

- 小写字母
- 下划线分隔
- 模块前缀：`sys_`、`gl_`、`bg_`、`fa_`、`wf_`

### 字段命名

- 小写字母
- 下划线分隔
- 必须字段：`id`、`created_time`、`updated_time`、`deleted`

### 索引命名

- 普通索引：`idx_字段名`
- 唯一索引：`uk_字段名`

## API规范

### RESTful风格

- GET: 查询
- POST: 创建
- PUT: 更新
- DELETE: 删除

### 统一响应

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1234567890
}
```

## 测试规范

### 单元测试

```java
@SpringBootTest
class XxxServiceTest {

    @Autowired
    private XxxService xxxService;

    @Test
    void testMethod() {
        // 测试代码
    }
}
```

## Git提交规范

### 提交信息格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type类型

- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式
- refactor: 重构
- perf: 性能优化
- test: 测试
- chore: 构建/工具

### 示例

```
feat(ledger): 添加凭证审核功能

- 添加凭证审核接口
- 添加凭证审核状态字段
- 添加凭证审核历史记录

Closes #123
```

## 代码审查

### 审查清单

- [ ] 代码符合规范
- [ ] 添加必要的注释
- [ ] 编写单元测试
- [ ] 更新API文档
- [ ] 无安全隐患
- [ ] 性能合理

## 性能优化建议

1. 使用索引优化查询
2. 避免N+1查询问题
3. 合理使用缓存
4. 批量操作使用批量API
5. 分页查询使用MyBatis Plus分页插件

## 安全建议

1. 输入参数校验
2. SQL注入防护
3. XSS防护
4. CSRF防护
5. 敏感数据加密

## 常用工具

### IDE

- IntelliJ IDEA（推荐）
- Eclipse

### 数据库工具

- Navicat
- DataGrip
- MySQL Workbench

### API测试

- Postman
- Apifox
- Swagger UI

### Git工具

- Git命令行
- SourceTree
- GitHub Desktop

## 联系方式

- 邮箱: dev@qoobot.com
- Issues: https://github.com/qoobot-com/openaccounting/issues
