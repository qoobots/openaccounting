-- 工作流模块数据库初始化脚本
-- 执行顺序: 在基础数据初始化后执行

-- 流程定义表
CREATE TABLE IF NOT EXISTS `wf_process_definition` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '流程定义ID',
    `company_id` BIGINT NOT NULL COMMENT '公司ID',
    `process_code` VARCHAR(50) NOT NULL COMMENT '流程编码',
    `process_name` VARCHAR(200) NOT NULL COMMENT '流程名称',
    `process_type` VARCHAR(50) COMMENT '流程类型',
    `version` INT DEFAULT 1 COMMENT '流程版本',
    `definition_json` TEXT COMMENT '流程定义JSON',
    `status` VARCHAR(20) DEFAULT '启用' COMMENT '状态',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_by` BIGINT COMMENT '创建人',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT COMMENT '更新人',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_process_code` (`process_code`),
    KEY `idx_company_id` (`company_id`),
    KEY `idx_process_type` (`process_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程定义表';

-- 流程实例表
CREATE TABLE IF NOT EXISTS `wf_process_instance` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '流程实例ID',
    `definition_id` BIGINT NOT NULL COMMENT '流程定义ID',
    `company_id` BIGINT NOT NULL COMMENT '公司ID',
    `business_type` VARCHAR(50) NOT NULL COMMENT '业务单据类型',
    `business_id` BIGINT NOT NULL COMMENT '业务单据ID',
    `instance_no` VARCHAR(50) NOT NULL COMMENT '流程实例编号',
    `initiator_id` BIGINT NOT NULL COMMENT '发起人ID',
    `initiator_name` VARCHAR(100) NOT NULL COMMENT '发起人',
    `start_time` DATETIME NOT NULL COMMENT '发起时间',
    `end_time` DATETIME COMMENT '完成时间',
    `current_node` VARCHAR(50) COMMENT '当前节点',
    `status` VARCHAR(20) DEFAULT '进行中' COMMENT '状态',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_by` BIGINT COMMENT '创建人',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT COMMENT '更新人',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_instance_no` (`instance_no`),
    KEY `idx_definition_id` (`definition_id`),
    KEY `idx_company_id` (`company_id`),
    KEY `idx_business` (`business_type`, `business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程实例表';

-- 任务表
CREATE TABLE IF NOT EXISTS `wf_task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `instance_id` BIGINT NOT NULL COMMENT '流程实例ID',
    `task_no` VARCHAR(50) NOT NULL COMMENT '任务编号',
    `task_name` VARCHAR(200) NOT NULL COMMENT '任务名称',
    `task_node` VARCHAR(50) NOT NULL COMMENT '任务节点',
    `assignee_id` BIGINT NOT NULL COMMENT '处理人ID',
    `assignee_name` VARCHAR(100) NOT NULL COMMENT '处理人',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `process_time` DATETIME COMMENT '处理时间',
    `status` VARCHAR(20) DEFAULT '待处理' COMMENT '任务状态',
    `comment` VARCHAR(500) COMMENT '处理意见',
    `result` VARCHAR(20) COMMENT '处理结果',
    `created_by` BIGINT COMMENT '创建人',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT COMMENT '更新人',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_no` (`task_no`),
    KEY `idx_instance_id` (`instance_id`),
    KEY `idx_assignee_id` (`assignee_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

-- 插入示例流程定义
INSERT INTO `wf_process_definition` (`company_id`, `process_code`, `process_name`, `process_type`, `definition_json`, `status`, `created_by`) VALUES
(1, 'VOUCHER_APPROVAL', '凭证审批流程', '凭证审批', '{"nodes":[{"id":"start","name":"开始"},{"id":"review","name":"审核"},{"id":"approve","name":"审批"},{"id":"end","name":"结束"}]}', '启用', 1),
(1, 'BUDGET_APPROVAL', '预算审批流程', '预算审批', '{"nodes":[{"id":"start","name":"开始"},{"id":"department_review","name":"部门审核"},{"id":"finance_review","name":"财务审核"},{"id":"approve","name":"审批"},{"id":"end","name":"结束"}]}', '启用', 1),
(1, 'ASSET_DISPOSAL', '资产处置审批', '资产处置', '{"nodes":[{"id":"start","name":"开始"},{"id":"department_approve","name":"部门审批"},{"id":"finance_approve","name":"财务审批"},{"id":"end","name":"结束"}]}', '启用', 1);
