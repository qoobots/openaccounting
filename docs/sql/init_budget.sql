-- 预算管理表

-- 预算主表
CREATE TABLE IF NOT EXISTS `bg_budget` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `company_id` BIGINT NOT NULL COMMENT '公司ID',
    `budget_code` VARCHAR(30) DEFAULT NULL COMMENT '预算编码',
    `budget_name` VARCHAR(100) NOT NULL COMMENT '预算名称',
    `budget_year` INT NOT NULL COMMENT '预算年度',
    `budget_type` VARCHAR(20) NOT NULL COMMENT '预算类型',
    `target_id` BIGINT DEFAULT NULL COMMENT '预算对象ID',
    `target_name` VARCHAR(100) DEFAULT NULL COMMENT '预算对象名称',
    `budget_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '预算金额',
    `start_date` DATE DEFAULT NULL COMMENT '开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '结束日期',
    `status` VARCHAR(20) DEFAULT 'draft' COMMENT '状态',
    `submitter_id` BIGINT DEFAULT NULL COMMENT '提交人ID',
    `submitter_name` VARCHAR(50) DEFAULT NULL COMMENT '提交人',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
    `approver_name` VARCHAR(50) DEFAULT NULL COMMENT '审批人',
    `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_company_code` (`company_id`, `budget_code`),
    KEY `idx_company_year` (`company_id`, `budget_year`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预算主表';

-- 预算明细表
CREATE TABLE IF NOT EXISTS `bg_budget_detail` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `budget_id` BIGINT NOT NULL COMMENT '预算ID',
    `account_id` BIGINT NOT NULL COMMENT '科目ID',
    `account_code` VARCHAR(20) NOT NULL COMMENT '科目编码',
    `account_name` VARCHAR(100) NOT NULL COMMENT '科目名称',
    `period` INT NOT NULL COMMENT '期间',
    `budget_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '预算金额',
    `actual_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '实际发生额',
    `difference` DECIMAL(18,2) DEFAULT 0.00 COMMENT '差异',
    `execution_rate` DECIMAL(10,4) DEFAULT 0.0000 COMMENT '执行率',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_budget_account_period` (`budget_id`, `account_id`, `period`),
    KEY `idx_budget_id` (`budget_id`),
    KEY `idx_account_id` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预算明细表';
