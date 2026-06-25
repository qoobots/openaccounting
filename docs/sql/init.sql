-- ============================================================================
-- 企业会计系统 - 初始化SQL脚本
-- ============================================================================
-- 创建时间: 2026-02-15
-- 数据库版本: MySQL 8.4
-- 字符集: utf8mb4
-- ============================================================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================================
-- 1. 系统管理模块 (sys_)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- sys_company - 公司信息表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `sys_company`;
CREATE TABLE `sys_company` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `company_code` VARCHAR(20) NOT NULL COMMENT '公司编码，唯一',
  `company_name` VARCHAR(100) NOT NULL COMMENT '公司名称',
  `short_name` VARCHAR(50) DEFAULT NULL COMMENT '公司简称',
  `legal_person` VARCHAR(50) DEFAULT NULL COMMENT '法人代表',
  `tax_no` VARCHAR(50) DEFAULT NULL COMMENT '税号',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用 0-停用',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记：0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_code` (`company_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公司信息表';

-- ----------------------------------------------------------------------------
-- sys_department - 部门信息表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `company_id` BIGINT NOT NULL COMMENT '公司ID',
  `dept_code` VARCHAR(20) NOT NULL COMMENT '部门编码',
  `dept_name` VARCHAR(100) NOT NULL COMMENT '部门名称',
  `parent_id` BIGINT DEFAULT 0 COMMENT '上级部门ID，0表示顶级',
  `level` INT NOT NULL DEFAULT 1 COMMENT '层级',
  `path` VARCHAR(500) DEFAULT NULL COMMENT '部门路径，如: /1/2/3',
  `leader_id` BIGINT DEFAULT NULL COMMENT '部门负责人ID',
  `leader_name` VARCHAR(50) DEFAULT NULL COMMENT '部门负责人姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用 0-停用',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门信息表';

-- ----------------------------------------------------------------------------
-- sys_user - 用户表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名，唯一',
  `password` VARCHAR(100) NOT NULL COMMENT '密码，BCrypt加密',
  `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `nick_name` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `avatar` VARCHAR(200) DEFAULT NULL COMMENT '头像URL',
  `gender` TINYINT NOT NULL DEFAULT 0 COMMENT '性别：0-未知 1-男 2-女',
  `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
  `company_id` BIGINT NOT NULL COMMENT '公司ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- ----------------------------------------------------------------------------
-- sys_role - 角色表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码，唯一',
  `role_name` VARCHAR(100) NOT NULL COMMENT '角色名称',
  `company_id` BIGINT DEFAULT NULL COMMENT '公司ID，NULL表示全局角色',
  `data_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '数据权限范围：1-全部 2-自定义 3-本部门及以下 4-本部门 5-仅本人',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`),
  KEY `idx_company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

-- ----------------------------------------------------------------------------
-- sys_permission - 权限表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码，唯一',
  `permission_name` VARCHAR(100) NOT NULL COMMENT '权限名称',
  `parent_id` BIGINT DEFAULT 0 COMMENT '上级权限ID',
  `permission_type` TINYINT NOT NULL COMMENT '权限类型：1-菜单 2-按钮 3-接口',
  `menu_url` VARCHAR(200) DEFAULT NULL COMMENT '菜单URL',
  `api_url` VARCHAR(200) DEFAULT NULL COMMENT '接口URL',
  `method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法：GET/POST/PUT/DELETE',
  `icon` VARCHAR(50) DEFAULT NULL COMMENT '菜单图标',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限表';

-- ----------------------------------------------------------------------------
-- sys_user_role - 用户角色关联表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';

-- ----------------------------------------------------------------------------
-- sys_role_permission - 角色权限关联表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT NOT NULL COMMENT '权限ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限关联表';

-- ----------------------------------------------------------------------------
-- sys_dict - 数据字典表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `dict_code` VARCHAR(50) NOT NULL COMMENT '字典编码，唯一',
  `dict_name` VARCHAR(100) NOT NULL COMMENT '字典名称',
  `dict_type` VARCHAR(50) DEFAULT NULL COMMENT '字典类型',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_code` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据字典表';

-- ----------------------------------------------------------------------------
-- sys_dict_item - 数据字典项表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `dict_id` BIGINT NOT NULL COMMENT '字典ID',
  `item_code` VARCHAR(50) NOT NULL COMMENT '字典项编码',
  `item_name` VARCHAR(100) NOT NULL COMMENT '字典项名称',
  `item_value` VARCHAR(200) NOT NULL COMMENT '字典项值',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_dict_id` (`dict_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据字典项表';

-- ============================================================================
-- 2. 总账模块 (gl_)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- gl_account - 科目表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `gl_account`;
CREATE TABLE `gl_account` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `company_id` BIGINT NOT NULL COMMENT '公司ID',
  `account_code` VARCHAR(20) NOT NULL COMMENT '科目编码',
  `account_name` VARCHAR(100) NOT NULL COMMENT '科目名称',
  `parent_id` BIGINT DEFAULT 0 COMMENT '上级科目ID，0表示顶级',
  `level` INT NOT NULL DEFAULT 1 COMMENT '层级：1-4',
  `account_type` VARCHAR(20) NOT NULL COMMENT '科目类型：ASSET-资产类 LIABILITY-负债类 EQUITY-所有者权益类 COST-成本类 INCOME-损益类',
  `balance_direction` VARCHAR(10) NOT NULL COMMENT '余额方向：DEBIT-借方 CREDIT-贷方',
  `auxiliary_dept` TINYINT NOT NULL DEFAULT 0 COMMENT '部门核算：1-是 0-否',
  `auxiliary_project` TINYINT NOT NULL DEFAULT 0 COMMENT '项目核算：1-是 0-否',
  `auxiliary_customer` TINYINT NOT NULL DEFAULT 0 COMMENT '客户核算：1-是 0-否',
  `auxiliary_supplier` TINYINT NOT NULL DEFAULT 0 COMMENT '供应商核算：1-是 0-否',
  `auxiliary_employee` TINYINT NOT NULL DEFAULT 0 COMMENT '员工核算：1-是 0-否',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用 0-停用',
  `is_leaf` TINYINT NOT NULL DEFAULT 1 COMMENT '是否末级：1-是 0-否',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_account_code` (`company_id`, `account_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_account_type` (`account_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='科目表';

-- ----------------------------------------------------------------------------
-- gl_voucher - 凭证表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `gl_voucher`;
CREATE TABLE `gl_voucher` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `company_id` BIGINT NOT NULL COMMENT '公司ID',
  `voucher_no` VARCHAR(30) NOT NULL COMMENT '凭证号',
  `accounting_year` INT NOT NULL COMMENT '会计年度',
  `accounting_period` INT NOT NULL COMMENT '会计期间',
  `voucher_date` DATE NOT NULL COMMENT '凭证日期',
  `voucher_type` VARCHAR(20) NOT NULL COMMENT '凭证类型：COMMON-通用凭证 CASH-现金凭证 BANK-银行凭证 TRANSFER-转账凭证',
  `abstract` VARCHAR(500) DEFAULT NULL COMMENT '摘要',
  `total_debit` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '借方合计',
  `total_credit` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '贷方合计',
  `entry_count` INT NOT NULL DEFAULT 0 COMMENT '分录数量',
  `currency` VARCHAR(10) NOT NULL DEFAULT 'CNY' COMMENT '币种',
  `status` VARCHAR(20) NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT-草稿 SUBMITTED-已提交 AUDITED-已审核 POSTED-已过账',
  `maker_id` BIGINT DEFAULT NULL COMMENT '制单人ID',
  `maker_name` VARCHAR(50) DEFAULT NULL COMMENT '制单人姓名',
  `submit_time` DATETIME DEFAULT NULL COMMENT '提交时间',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `auditor_id` BIGINT DEFAULT NULL COMMENT '审核人ID',
  `auditor_name` VARCHAR(50) DEFAULT NULL COMMENT '审核人姓名',
  `audit_opinion` VARCHAR(500) DEFAULT NULL COMMENT '审核意见',
  `post_time` DATETIME DEFAULT NULL COMMENT '过账时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_voucher_no` (`company_id`, `voucher_no`),
  KEY `idx_company_period` (`company_id`, `accounting_year`, `accounting_period`),
  KEY `idx_status` (`status`),
  KEY `idx_voucher_date` (`voucher_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='凭证表';

-- ----------------------------------------------------------------------------
-- gl_voucher_entry - 凭证分录表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `gl_voucher_entry`;
CREATE TABLE `gl_voucher_entry` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `voucher_id` BIGINT NOT NULL COMMENT '凭证ID',
  `line_no` INT NOT NULL COMMENT '行号',
  `account_id` BIGINT NOT NULL COMMENT '科目ID',
  `account_code` VARCHAR(20) NOT NULL COMMENT '科目编码',
  `account_name` VARCHAR(100) NOT NULL COMMENT '科目名称',
  `abstract` VARCHAR(500) DEFAULT NULL COMMENT '摘要',
  `debit_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '借方金额',
  `credit_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '贷方金额',
  `currency` VARCHAR(10) NOT NULL DEFAULT 'CNY' COMMENT '币种',
  `exchange_rate` DECIMAL(10,6) NOT NULL DEFAULT 1.000000 COMMENT '汇率',
  `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
  `project_id` BIGINT DEFAULT NULL COMMENT '项目ID',
  `customer_id` BIGINT DEFAULT NULL COMMENT '客户ID',
  `supplier_id` BIGINT DEFAULT NULL COMMENT '供应商ID',
  `employee_id` BIGINT DEFAULT NULL COMMENT '员工ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_voucher_id` (`voucher_id`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='凭证分录表';

-- ----------------------------------------------------------------------------
-- gl_balance - 总账余额表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `gl_balance`;
CREATE TABLE `gl_balance` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `company_id` BIGINT NOT NULL COMMENT '公司ID',
  `account_id` BIGINT NOT NULL COMMENT '科目ID',
  `account_code` VARCHAR(20) NOT NULL COMMENT '科目编码',
  `accounting_year` INT NOT NULL COMMENT '会计年度',
  `accounting_period` INT NOT NULL COMMENT '会计期间',
  `beginning_balance` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '期初余额',
  `debit_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '借方发生额',
  `credit_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '贷方发生额',
  `ending_balance` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '期末余额',
  `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
  `project_id` BIGINT DEFAULT NULL COMMENT '项目ID',
  `customer_id` BIGINT DEFAULT NULL COMMENT '客户ID',
  `supplier_id` BIGINT DEFAULT NULL COMMENT '供应商ID',
  `employee_id` BIGINT DEFAULT NULL COMMENT '员工ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_balance_unique` (`company_id`, `account_id`, `accounting_year`, `accounting_period`, `dept_id`, `project_id`, `customer_id`, `supplier_id`, `employee_id`),
  KEY `idx_company_period` (`company_id`, `accounting_year`, `accounting_period`),
  KEY `idx_account_id` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='总账余额表';

-- ----------------------------------------------------------------------------
-- gl_accounting_period - 会计期间表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `gl_accounting_period`;
CREATE TABLE `gl_accounting_period` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `company_id` BIGINT NOT NULL COMMENT '公司ID',
  `accounting_year` INT NOT NULL COMMENT '会计年度',
  `accounting_period` INT NOT NULL COMMENT '会计期间',
  `period_start_date` DATE NOT NULL COMMENT '期间开始日期',
  `period_end_date` DATE NOT NULL COMMENT '期间结束日期',
  `status` VARCHAR(20) NOT NULL DEFAULT 'OPEN' COMMENT '状态：OPEN-开启 CLOSED-关闭',
  `close_time` DATETIME DEFAULT NULL COMMENT '关闭时间',
  `close_by` VARCHAR(50) DEFAULT NULL COMMENT '关闭人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_period` (`company_id`, `accounting_year`, `accounting_period`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会计期间表';

-- ============================================================================
-- 3. 基础档案模块 (ba_)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- ba_customer - 客户表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `ba_customer`;
CREATE TABLE `ba_customer` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `company_id` BIGINT NOT NULL COMMENT '公司ID',
  `customer_code` VARCHAR(20) NOT NULL COMMENT '客户编码',
  `customer_name` VARCHAR(100) NOT NULL COMMENT '客户名称',
  `short_name` VARCHAR(50) DEFAULT NULL COMMENT '简称',
  `customer_type` VARCHAR(20) DEFAULT NULL COMMENT '客户类型',
  `tax_no` VARCHAR(50) DEFAULT NULL COMMENT '税号',
  `contact` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
  `credit_limit` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '信用额度',
  `credit_days` INT DEFAULT NULL COMMENT '信用天数',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-停用',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_customer_code` (`company_id`, `customer_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='客户表';

-- ----------------------------------------------------------------------------
-- ba_supplier - 供应商表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `ba_supplier`;
CREATE TABLE `ba_supplier` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `company_id` BIGINT NOT NULL COMMENT '公司ID',
  `supplier_code` VARCHAR(20) NOT NULL COMMENT '供应商编码',
  `supplier_name` VARCHAR(100) NOT NULL COMMENT '供应商名称',
  `short_name` VARCHAR(50) DEFAULT NULL COMMENT '简称',
  `supplier_type` VARCHAR(20) DEFAULT NULL COMMENT '供应商类型',
  `tax_no` VARCHAR(50) DEFAULT NULL COMMENT '税号',
  `contact` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
  `credit_limit` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '信用额度',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-停用',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_supplier_code` (`company_id`, `supplier_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='供应商表';

-- ----------------------------------------------------------------------------
-- ba_project - 项目表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `ba_project`;
CREATE TABLE `ba_project` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `company_id` BIGINT NOT NULL COMMENT '公司ID',
  `project_code` VARCHAR(20) NOT NULL COMMENT '项目编码',
  `project_name` VARCHAR(100) NOT NULL COMMENT '项目名称',
  `project_type` VARCHAR(20) DEFAULT NULL COMMENT '项目类型',
  `manager_id` BIGINT DEFAULT NULL COMMENT '项目经理ID',
  `manager_name` VARCHAR(50) DEFAULT NULL COMMENT '项目经理姓名',
  `start_date` DATE DEFAULT NULL COMMENT '开始日期',
  `end_date` DATE DEFAULT NULL COMMENT '结束日期',
  `budget_amount` DECIMAL(18,2) DEFAULT NULL COMMENT '预算金额',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PLANNING' COMMENT '状态：PLANNING-计划中 IN_PROGRESS-进行中 COMPLETED-已完成 CANCELLED-已取消',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_project_code` (`company_id`, `project_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目表';

-- ============================================================================
-- 4. 预算管理模块 (bg_)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- bg_budget - 预算表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `bg_budget`;
CREATE TABLE `bg_budget` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `company_id` BIGINT NOT NULL COMMENT '公司ID',
  `budget_code` VARCHAR(20) NOT NULL COMMENT '预算编码',
  `budget_name` VARCHAR(100) NOT NULL COMMENT '预算名称',
  `budget_year` INT NOT NULL COMMENT '预算年度',
  `version` VARCHAR(20) NOT NULL DEFAULT 'V1.0' COMMENT '预算版本',
  `status` VARCHAR(20) NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT-草稿 PENDING-待审批 APPROVED-已生效 CANCELLED-已取消',
  `submit_time` DATETIME DEFAULT NULL COMMENT '提交时间',
  `submit_by` VARCHAR(50) DEFAULT NULL COMMENT '提交人',
  `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
  `approve_by` VARCHAR(50) DEFAULT NULL COMMENT '审批人',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_budget_version` (`company_id`, `budget_code`, `version`),
  KEY `idx_budget_year` (`budget_year`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='预算表';

-- ----------------------------------------------------------------------------
-- bg_budget_detail - 预算明细表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `bg_budget_detail`;
CREATE TABLE `bg_budget_detail` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `budget_id` BIGINT NOT NULL COMMENT '预算ID',
  `account_id` BIGINT NOT NULL COMMENT '科目ID',
  `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
  `project_id` BIGINT DEFAULT NULL COMMENT '项目ID',
  `year_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '年度预算',
  `q1_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '一季度预算',
  `q2_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '二季度预算',
  `q3_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '三季度预算',
  `q4_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '四季度预算',
  `actual_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '实际金额',
  `execution_rate` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '执行率',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_budget_id` (`budget_id`),
  KEY `idx_account_id` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='预算明细表';

-- ============================================================================
-- 5. 固定资产模块 (fa_)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- fa_asset - 资产卡片表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `fa_asset`;
CREATE TABLE `fa_asset` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `company_id` BIGINT NOT NULL COMMENT '公司ID',
  `asset_code` VARCHAR(20) NOT NULL COMMENT '资产编码',
  `asset_name` VARCHAR(100) NOT NULL COMMENT '资产名称',
  `asset_type` VARCHAR(20) NOT NULL COMMENT '资产类别',
  `asset_category` VARCHAR(50) DEFAULT NULL COMMENT '资产分类',
  `spec_model` VARCHAR(100) DEFAULT NULL COMMENT '规格型号',
  `dept_id` BIGINT DEFAULT NULL COMMENT '使用部门ID',
  `dept_name` VARCHAR(100) DEFAULT NULL COMMENT '使用部门名称',
  `location` VARCHAR(200) DEFAULT NULL COMMENT '存放地点',
  `purchase_date` DATE DEFAULT NULL COMMENT '购置日期',
  `start_use_date` DATE DEFAULT NULL COMMENT '启用日期',
  `original_value` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '原值',
  `net_value` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '净值',
  `accumulated_depreciation` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '累计折旧',
  `useful_life` INT DEFAULT NULL COMMENT '使用年限（月）',
  `depreciation_method` VARCHAR(20) NOT NULL DEFAULT 'STRAIGHT' COMMENT '折旧方法：STRAIGHT-直线法 DBDDBL-双倍余额递减法 SYD-年数总和法',
  `monthly_depreciation` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '月折旧额',
  `total_depreciated_months` INT NOT NULL DEFAULT 0 COMMENT '已折旧月数',
  `supplier_id` BIGINT DEFAULT NULL COMMENT '供应商ID',
  `invoice_no` VARCHAR(50) DEFAULT NULL COMMENT '发票号',
  `status` VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT '状态：NORMAL-正常 SCRAPPED-报废 TRANSFERRED-调拨',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_asset_code` (`company_id`, `asset_code`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产卡片表';

-- ----------------------------------------------------------------------------
-- fa_depreciation - 资产折旧表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `fa_depreciation`;
CREATE TABLE `fa_depreciation` (
  `id` BIGINT NOT NULL COMMENT '主键，雪花ID',
  `asset_id` BIGINT NOT NULL COMMENT '资产ID',
  `depreciation_period` VARCHAR(20) NOT NULL COMMENT '折旧期间（YYYYMM）',
  `depreciation_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '折旧金额',
  `voucher_id` BIGINT DEFAULT NULL COMMENT '凭证ID',
  `voucher_no` VARCHAR(30) DEFAULT NULL COMMENT '凭证号',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_asset_period` (`asset_id`, `depreciation_period`),
  KEY `idx_depreciation_period` (`depreciation_period`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产折旧表';

-- ============================================================================
-- 6. 初始化数据
-- ============================================================================

-- 插入默认公司
INSERT INTO `sys_company` (`id`, `company_code`, `company_name`, `short_name`, `legal_person`, `status`) VALUES
(1, 'DEFAULT', '默认公司', '默认', 'admin', 1);

-- 插入默认管理员用户
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `nick_name`, `gender`, `company_id`, `status`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 'admin', 1, 1, 1);

-- 插入默认角色
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `data_scope`, `status`, `sort`) VALUES
(1, 'SUPER_ADMIN', '超级管理员', 1, 1, 0),
(2, 'ADMIN', '管理员', 3, 1, 1),
(3, 'USER', '普通用户', 5, 1, 2);

-- 插入默认权限
INSERT INTO `sys_permission` (`id`, `permission_code`, `permission_name`, `parent_id`, `permission_type`, `sort`, `status`) VALUES
(1, 'system', '系统管理', 0, 1, 1, 1),
(2, 'system:user', '用户管理', 1, 1, 1, 1),
(3, 'system:role', '角色管理', 1, 1, 2, 1),
(4, 'system:permission', '权限管理', 1, 1, 3, 1),
(5, 'ledger', '总账管理', 0, 1, 2, 1),
(6, 'ledger:account', '科目管理', 5, 1, 1, 1),
(7, 'ledger:voucher', '凭证管理', 5, 1, 2, 1),
(8, 'ledger:book', '账簿查询', 5, 1, 3, 1);

-- 插入用户角色关联
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES
(1, 1, 1);

-- 插入角色权限关联（超级管理员拥有所有权限）
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 1, 5),
(6, 1, 6),
(7, 1, 7),
(8, 1, 8);

-- 插入默认数据字典
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `dict_type`, `status`) VALUES
(1, 'yes_no', '是否', 'SYSTEM', 1),
(2, 'status', '状态', 'SYSTEM', 1),
(3, 'gender', '性别', 'SYSTEM', 1);

-- 插入数据字典项
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_code`, `item_name`, `item_value`, `sort`, `status`) VALUES
(1, 1, 'YES', '是', '1', 1, 1),
(2, 1, 'NO', '否', '0', 2, 1),
(3, 2, 'ENABLE', '启用', '1', 1, 1),
(4, 2, 'DISABLE', '禁用', '0', 2, 1),
(5, 3, 'MALE', '男', '1', 1, 1),
(6, 3, 'FEMALE', '女', '2', 2, 1),
(7, 3, 'UNKNOWN', '未知', '0', 3, 1);

SET FOREIGN_KEY_CHECKS = 1;
