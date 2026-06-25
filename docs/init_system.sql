-- 初始化系统管理模块的数据库表和初始数据
-- 执行前请确保已创建数据库: openaccounting

USE openaccounting;

-- =============================================
-- 1. 公司信息表
-- =============================================
CREATE TABLE IF NOT EXISTS `sys_company` (
    `id` BIGINT(20) NOT NULL COMMENT '公司ID',
    `parent_id` BIGINT(20) DEFAULT 0 COMMENT '父公司ID',
    `ancestors` VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    `company_code` VARCHAR(50) NOT NULL COMMENT '公司编码',
    `company_name` VARCHAR(100) NOT NULL COMMENT '公司名称',
    `short_name` VARCHAR(50) DEFAULT NULL COMMENT '公司简称',
    `social_credit_code` VARCHAR(50) DEFAULT NULL COMMENT '统一社会信用代码',
    `legal_person` VARCHAR(50) DEFAULT NULL COMMENT '法定代表人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
    `sort` INT(4) DEFAULT 0 COMMENT '显示顺序',
    `status` CHAR(1) DEFAULT '0' COMMENT '公司状态（0正常 1停用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_company_code` (`company_code`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公司信息表';

-- =============================================
-- 2. 部门信息表
-- =============================================
CREATE TABLE IF NOT EXISTS `sys_department` (
    `id` BIGINT(20) NOT NULL COMMENT '部门ID',
    `parent_id` BIGINT(20) DEFAULT 0 COMMENT '父部门ID',
    `ancestors` VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    `department_name` VARCHAR(30) DEFAULT '' COMMENT '部门名称',
    `department_code` VARCHAR(30) DEFAULT '' COMMENT '部门编码',
    `leader` VARCHAR(20) DEFAULT NULL COMMENT '负责人',
    `phone` VARCHAR(11) DEFAULT NULL COMMENT '联系电话',
    `sort` INT(4) DEFAULT 0 COMMENT '显示顺序',
    `status` CHAR(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dept_code` (`department_code`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门信息表';

-- =============================================
-- 3. 用户信息表
-- =============================================
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `username` VARCHAR(30) NOT NULL COMMENT '用户账号',
    `real_name` VARCHAR(30) DEFAULT NULL COMMENT '用户姓名',
    `nick_name` VARCHAR(30) DEFAULT NULL COMMENT '用户昵称',
    `password` VARCHAR(100) DEFAULT '' COMMENT '密码',
    `email` VARCHAR(50) DEFAULT '' COMMENT '用户邮箱',
    `phone` VARCHAR(11) DEFAULT '' COMMENT '手机号码',
    `gender` CHAR(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
    `avatar` VARCHAR(100) DEFAULT '' COMMENT '头像地址',
    `dept_id` BIGINT(20) DEFAULT NULL COMMENT '部门ID',
    `company_id` BIGINT(20) DEFAULT NULL COMMENT '公司ID',
    `status` CHAR(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- =============================================
-- 4. 角色信息表
-- =============================================
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` BIGINT(20) NOT NULL COMMENT '角色ID',
    `role_name` VARCHAR(30) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(30) NOT NULL COMMENT '角色权限字符串',
    `role_sort` INT(4) DEFAULT 0 COMMENT '显示顺序',
    `data_scope` CHAR(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定义数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    `status` CHAR(1) DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- =============================================
-- 5. 菜单权限表
-- =============================================
CREATE TABLE IF NOT EXISTS `sys_permission` (
    `id` BIGINT(20) NOT NULL COMMENT '菜单ID',
    `parent_id` BIGINT(20) DEFAULT 0 COMMENT '父菜单ID',
    `ancestors` VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    `permission_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `permission_code` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    `permission_type` CHAR(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `path` VARCHAR(200) DEFAULT '#' COMMENT '路由地址',
    `component` VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    `icon` VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
    `method` VARCHAR(10) DEFAULT '' COMMENT '请求方式',
    `sort` INT(4) DEFAULT 0 COMMENT '显示顺序',
    `status` CHAR(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
    `remark` VARCHAR(500) DEFAULT '' COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- =============================================
-- 6. 用户和角色关联表
-- =============================================
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id` BIGINT(20) NOT NULL COMMENT '主键ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

-- =============================================
-- 7. 角色和菜单关联表
-- =============================================
CREATE TABLE IF NOT EXISTS `sys_role_permission` (
    `id` BIGINT(20) NOT NULL COMMENT '主键ID',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

-- =============================================
-- 初始化数据
-- =============================================

-- 初始化公司
INSERT INTO `sys_company` VALUES (1, 0, '0', 'QOO001', 'QOO机器人科技有限公司', 'QOO', '91110000XXXXXXXXXX', '张三', '13800138000', 'admin@qoobot.com', '北京市海淀区', 0, '0', '总公司', 'admin', NOW(), 'admin', NOW());

-- 初始化部门
INSERT INTO `sys_department` VALUES (1, 0, '0', '总公司', 'DEPT001', '张三', '13800138000', 0, '0', '总公司', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_department` VALUES (100, 1, '0,1', '研发部', 'DEPT100', '李四', '13800138001', 1, '0', '研发部门', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_department` VALUES (101, 1, '0,1', '市场部', 'DEPT101', '王五', '13800138002', 2, '0', '市场部门', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_department` VALUES (102, 1, '0,1', '财务部', 'DEPT102', '赵六', '13800138003', 3, '0', '财务部门', 'admin', NOW(), 'admin', NOW());

-- 初始化角色
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', '0', '超级管理员', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_role` VALUES (2, '普通用户', 'user', 2, '2', '0', '普通用户', 'admin', NOW(), 'admin', NOW());

-- 初始化用户 (密码: admin123)
INSERT INTO `sys_user` VALUES (1, 'admin', '管理员', 'admin', '$2a$10$7JB720yubVSazv0oI0YQO.HKb4HfFf3WQ8K9Z2Z5X6Y8Z1A3B5C7', 'admin@qoobot.com', '13800138000', '0', '', 1, 1, '0', '管理员', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_user` VALUES (2, 'user', '普通用户', 'user', '$2a$10$7JB720yubVSazv0oI0YQO.HKb4HfFf3WQ8K9Z2Z5X6Y8Z1A3B5C7', 'user@qoobot.com', '13800138001', '0', '', 100, 1, '0', '普通用户', 'admin', NOW(), 'admin', NOW());

-- 初始化用户角色关联
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2, 2);

-- 初始化菜单权限
INSERT INTO `sys_permission` VALUES (1, 0, '0', '系统管理', '', 'M', '/system', NULL, 'setting', '', 1, '0', '系统管理目录', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_permission` VALUES (2, 1, '0,1', '用户管理', 'system:user:list', 'C', '/system/user', 'system/user/index', 'user', '', 1, '0', '用户管理菜单', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_permission` VALUES (3, 1, '0,1', '角色管理', 'system:role:list', 'C', '/system/role', 'system/role/index', 'team', '', 2, '0', '角色管理菜单', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_permission` VALUES (4, 1, '0,1', '部门管理', 'system:dept:list', 'C', '/system/dept', 'system/dept/index', 'apartment', '', 3, '0', '部门管理菜单', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_permission` VALUES (5, 1, '0,1', '公司管理', 'system:company:list', 'C', '/system/company', 'system/company/index', 'building', '', 4, '0', '公司管理菜单', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_permission` VALUES (6, 1, '0,1', '菜单管理', 'system:menu:list', 'C', '/system/menu', 'system/menu/index', 'menu', '', 5, '0', '菜单管理菜单', 'admin', NOW(), 'admin', NOW());

-- 初始化按钮权限
INSERT INTO `sys_permission` VALUES (100, 2, '0,1,2', '用户查询', 'system:user:query', 'F', '', '', '', 'GET', 1, '0', '', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_permission` VALUES (101, 2, '0,1,2', '用户新增', 'system:user:add', 'F', '', '', '', 'POST', 2, '0', '', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_permission` VALUES (102, 2, '0,1,2', '用户修改', 'system:user:edit', 'F', '', '', '', 'PUT', 3, '0', '', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_permission` VALUES (103, 2, '0,1,2', '用户删除', 'system:user:remove', 'F', '', '', '', 'DELETE', 4, '0', '', 'admin', NOW(), 'admin', NOW());
INSERT INTO `sys_permission` VALUES (104, 2, '0,1,2', '用户导出', 'system:user:export', 'F', '', '', '', 'GET', 5, '0', '', 'admin', NOW(), 'admin', NOW());

-- 初始化角色菜单关联 (超级管理员拥有所有权限)
INSERT INTO `sys_role_permission` VALUES (1, 1, 1);
INSERT INTO `sys_role_permission` VALUES (2, 1, 2);
INSERT INTO `sys_role_permission` VALUES (3, 1, 3);
INSERT INTO `sys_role_permission` VALUES (4, 1, 4);
INSERT INTO `sys_role_permission` VALUES (5, 1, 5);
INSERT INTO `sys_role_permission` VALUES (6, 1, 6);
INSERT INTO `sys_role_permission` VALUES (7, 1, 100);
INSERT INTO `sys_role_permission` VALUES (8, 1, 101);
INSERT INTO `sys_role_permission` VALUES (9, 1, 102);
INSERT INTO `sys_role_permission` VALUES (10, 1, 103);
INSERT INTO `sys_role_permission` VALUES (11, 1, 104);
