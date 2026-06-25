-- 资产管理模块数据库初始化脚本
-- 执行顺序: 在基础数据初始化后执行

-- 固定资产表
CREATE TABLE IF NOT EXISTS `fa_asset` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '资产ID',
    `company_id` BIGINT NOT NULL COMMENT '公司ID',
    `asset_code` VARCHAR(50) NOT NULL COMMENT '资产编码',
    `asset_name` VARCHAR(200) NOT NULL COMMENT '资产名称',
    `asset_category` VARCHAR(100) COMMENT '资产类别',
    `specification` VARCHAR(200) COMMENT '资产规格',
    `model` VARCHAR(100) COMMENT '资产型号',
    `department_id` BIGINT COMMENT '使用部门ID',
    `department_name` VARCHAR(100) COMMENT '使用部门',
    `responsible_person_id` BIGINT COMMENT '责任人ID',
    `responsible_person_name` VARCHAR(100) COMMENT '责任人',
    `location` VARCHAR(200) COMMENT '存放地点',
    `original_value` DECIMAL(18,2) NOT NULL COMMENT '资产原值',
    `purchase_date` DATE NOT NULL COMMENT '购置日期',
    `start_date` DATE COMMENT '启用日期',
    `depreciation_method` VARCHAR(50) DEFAULT '直线法' COMMENT '折旧方法',
    `depreciation_years` INT DEFAULT 5 COMMENT '折旧年限',
    `salvage_rate` DECIMAL(5,2) DEFAULT 5.00 COMMENT '残值率(%)',
    `salvage_value` DECIMAL(18,2) COMMENT '残值',
    `accumulated_depreciation` DECIMAL(18,2) DEFAULT 0.00 COMMENT '累计折旧',
    `net_value` DECIMAL(18,2) COMMENT '净值',
    `asset_status` VARCHAR(20) DEFAULT '在用' COMMENT '资产状态',
    `supplier` VARCHAR(200) COMMENT '供应商',
    `invoice_number` VARCHAR(100) COMMENT '发票号',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_by` BIGINT COMMENT '创建人',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT COMMENT '更新人',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_asset_code` (`asset_code`),
    KEY `idx_company_id` (`company_id`),
    KEY `idx_asset_category` (`asset_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='固定资产表';

-- 资产折旧记录表
CREATE TABLE IF NOT EXISTS `fa_depreciation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '折旧ID',
    `company_id` BIGINT NOT NULL COMMENT '公司ID',
    `asset_id` BIGINT NOT NULL COMMENT '资产ID',
    `asset_code` VARCHAR(50) NOT NULL COMMENT '资产编码',
    `asset_name` VARCHAR(200) NOT NULL COMMENT '资产名称',
    `depreciation_year` INT NOT NULL COMMENT '折旧年度',
    `depreciation_period` INT NOT NULL COMMENT '折旧期间',
    `depreciation_date` DATE NOT NULL COMMENT '折旧日期',
    `beginning_value` DECIMAL(18,2) NOT NULL COMMENT '期初原值',
    `current_depreciation` DECIMAL(18,2) NOT NULL COMMENT '本期折旧',
    `accumulated_depreciation` DECIMAL(18,2) NOT NULL COMMENT '累计折旧',
    `ending_net_value` DECIMAL(18,2) NOT NULL COMMENT '期末净值',
    `voucher_id` BIGINT COMMENT '凭证ID',
    `status` VARCHAR(20) DEFAULT '已提' COMMENT '状态',
    `created_by` BIGINT COMMENT '创建人',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT COMMENT '更新人',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`),
    KEY `idx_asset_id` (`asset_id`),
    KEY `idx_company_id` (`company_id`),
    KEY `idx_year_period` (`depreciation_year`, `depreciation_period`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产折旧记录表';

-- 资产盘点记录表
CREATE TABLE IF NOT EXISTS `fa_inventory` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '盘点ID',
    `company_id` BIGINT NOT NULL COMMENT '公司ID',
    `inventory_no` VARCHAR(50) NOT NULL COMMENT '盘点单号',
    `inventory_year` INT NOT NULL COMMENT '盘点年度',
    `inventory_period` INT NOT NULL COMMENT '盘点期间',
    `inventory_date` DATE NOT NULL COMMENT '盘点日期',
    `inventory_user_id` BIGINT COMMENT '盘点人ID',
    `inventory_user_name` VARCHAR(100) COMMENT '盘点人',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_by` BIGINT COMMENT '创建人',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT COMMENT '更新人',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_inventory_no` (`inventory_no`),
    KEY `idx_company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产盘点记录表';

-- 资产盘点明细表
CREATE TABLE IF NOT EXISTS `fa_inventory_detail` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '明细ID',
    `inventory_id` BIGINT NOT NULL COMMENT '盘点ID',
    `asset_id` BIGINT NOT NULL COMMENT '资产ID',
    `asset_code` VARCHAR(50) NOT NULL COMMENT '资产编码',
    `asset_name` VARCHAR(200) NOT NULL COMMENT '资产名称',
    `book_quantity` INT DEFAULT 1 COMMENT '账面数量',
    `actual_quantity` INT DEFAULT 1 COMMENT '盘点数量',
    `profit_quantity` INT DEFAULT 0 COMMENT '盘盈数量',
    `loss_quantity` INT DEFAULT 0 COMMENT '盘亏数量',
    `book_value` DECIMAL(18,2) COMMENT '账面价值',
    `actual_value` DECIMAL(18,2) COMMENT '实际价值',
    `profit_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '盘盈金额',
    `loss_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '盘亏金额',
    `inventory_result` VARCHAR(20) COMMENT '盘点结果',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_by` BIGINT COMMENT '创建人',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT COMMENT '更新人',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`),
    KEY `idx_inventory_id` (`inventory_id`),
    KEY `idx_asset_id` (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产盘点明细表';

-- 插入示例数据
INSERT INTO `fa_asset` (`company_id`, `asset_code`, `asset_name`, `asset_category`, `specification`, `department_id`, `department_name`, `location`, `original_value`, `purchase_date`, `start_date`, `depreciation_method`, `depreciation_years`, `salvage_rate`, `salvage_value`, `accumulated_depreciation`, `net_value`, `asset_status`, `created_by`) VALUES
(1, 'FA2025001', '笔记本电脑', '电子设备', 'ThinkPad X1 Carbon', 1, '技术部', '技术部办公室', 8000.00, '2025-01-10', '2025-01-15', '直线法', 3, 5.00, 400.00, 0.00, 8000.00, '在用', 1),
(1, 'FA2025002', '打印机', '办公设备', 'HP LaserJet Pro', 2, '行政部', '行政部办公室', 3500.00, '2025-02-01', '2025-02-05', '直线法', 5, 5.00, 175.00, 0.00, 3500.00, '在用', 1),
(1, 'FA2025003', '办公桌', '家具', '标准办公桌', 2, '行政部', '行政部办公室', 1200.00, '2025-01-20', '2025-01-25', '直线法', 5, 5.00, 60.00, 0.00, 1200.00, '在用', 1);
