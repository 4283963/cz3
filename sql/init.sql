-- =====================================================
-- 北京医保跨省异地就医结算系统数据库初始化脚本
-- Database: MySQL 8.0+
-- =====================================================

CREATE DATABASE IF NOT EXISTS bj_medical_insurance DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bj_medical_insurance;

-- =====================================================
-- 1. 参保人信息表
-- =====================================================
DROP TABLE IF EXISTS `insured_person`;
CREATE TABLE `insured_person` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `id_card` varchar(32) NOT NULL COMMENT '身份证号',
  `name` varchar(64) NOT NULL COMMENT '姓名',
  `gender` varchar(8) COMMENT '性别',
  `birth_date` date COMMENT '出生日期',
  `insurance_type` varchar(16) NOT NULL COMMENT '医保类型(1:职工医保 2:居民医保 3:退休人员医保)',
  `insurance_status` varchar(16) COMMENT '参保状态',
  `account_balance` decimal(12,2) DEFAULT 0 COMMENT '账户余额',
  `cumulative_payment` decimal(12,2) DEFAULT 0 COMMENT '累计缴费',
  `work_unit` varchar(128) COMMENT '工作单位',
  `contact_phone` varchar(32) COMMENT '联系电话',
  `address` varchar(256) COMMENT '地址',
  `personal_account_no` varchar(32) COMMENT '个人账户编号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_id_card` (`id_card`),
  KEY `idx_personal_account_no` (`personal_account_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参保人信息表';

-- =====================================================
-- 2. 个人账户表
-- =====================================================
DROP TABLE IF EXISTS `personal_account`;
CREATE TABLE `personal_account` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `personal_account_no` varchar(32) NOT NULL COMMENT '个人账户编号',
  `id_card` varchar(32) NOT NULL COMMENT '身份证号',
  `balance` decimal(12,2) DEFAULT 0 COMMENT '账户余额',
  `frozen_amount` decimal(12,2) DEFAULT 0 COMMENT '冻结金额',
  `available_balance` decimal(12,2) DEFAULT 0 COMMENT '可用余额',
  `total_income` decimal(12,2) DEFAULT 0 COMMENT '累计收入',
  `total_expense` decimal(12,2) DEFAULT 0 COMMENT '累计支出',
  `last_settlement_date` date COMMENT '最后结算日期',
  `account_status` varchar(16) DEFAULT 'NORMAL' COMMENT '账户状态(NORMAL:正常 FROZEN:冻结 CANCELLED:注销)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_personal_account_no` (`personal_account_no`),
  KEY `idx_id_card` (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='个人账户表';

-- =====================================================
-- 3. 医保政策表
-- =====================================================
DROP TABLE IF EXISTS `medical_policy`;
CREATE TABLE `medical_policy` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `policy_code` varchar(32) NOT NULL COMMENT '政策编码',
  `policy_name` varchar(128) NOT NULL COMMENT '政策名称',
  `insurance_type` varchar(16) NOT NULL COMMENT '医保类型',
  `treatment_type` varchar(32) NOT NULL COMMENT '就医类型(门诊/住院/特殊病种)',
  `deductible` decimal(12,2) DEFAULT 0 COMMENT '起付线',
  `reimbursement_ratio` decimal(5,2) DEFAULT 0 COMMENT '报销比例(%)',
  `max_reimbursement_amount` decimal(12,2) DEFAULT 0 COMMENT '年度最高支付限额',
  `applicable_scope` varchar(256) COMMENT '适用范围',
  `effective_date` date COMMENT '生效日期',
  `expiry_date` date COMMENT '失效日期',
  `policy_status` varchar(16) DEFAULT 'ACTIVE' COMMENT '政策状态(ACTIVE:生效 INACTIVE:失效)',
  `description` varchar(512) COMMENT '政策说明',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_policy_code` (`policy_code`),
  KEY `idx_insurance_type` (`insurance_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='医保政策表';

-- =====================================================
-- 4. 跨省定点医院表
-- =====================================================
DROP TABLE IF EXISTS `cross_province_hospital`;
CREATE TABLE `cross_province_hospital` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `hospital_code` varchar(32) NOT NULL COMMENT '医院编码',
  `hospital_name` varchar(128) NOT NULL COMMENT '医院名称',
  `province_code` varchar(16) NOT NULL COMMENT '省份编码',
  `province_name` varchar(64) NOT NULL COMMENT '省份名称',
  `city_code` varchar(16) COMMENT '城市编码',
  `city_name` varchar(64) COMMENT '城市名称',
  `hospital_level` varchar(16) COMMENT '医院等级(三级甲等/三级乙等...)',
  `hospital_type` varchar(32) COMMENT '医院类型(综合/专科/中医...)',
  `contact_person` varchar(64) COMMENT '联系人',
  `contact_phone` varchar(32) COMMENT '联系电话',
  `address` varchar(256) COMMENT '地址',
  `settlement_status` varchar(16) DEFAULT 'ACTIVE' COMMENT '结算状态(ACTIVE:可结算 SUSPENDED:暂停结算)',
  `interface_url` varchar(256) COMMENT '接口地址',
  `app_id` varchar(64) COMMENT '应用ID',
  `app_secret` varchar(128) COMMENT '应用密钥',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_hospital_code` (`hospital_code`),
  KEY `idx_province_code` (`province_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='跨省定点医院表';

-- =====================================================
-- 5. 就医记录表
-- =====================================================
DROP TABLE IF EXISTS `medical_record`;
CREATE TABLE `medical_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `record_no` varchar(32) NOT NULL COMMENT '就医记录编号',
  `id_card` varchar(32) NOT NULL COMMENT '身份证号',
  `name` varchar(64) NOT NULL COMMENT '姓名',
  `hospital_code` varchar(32) NOT NULL COMMENT '医院编码',
  `hospital_name` varchar(128) COMMENT '医院名称',
  `province_code` varchar(16) COMMENT '省份编码',
  `province_name` varchar(64) COMMENT '省份名称',
  `medical_type` varchar(32) NOT NULL COMMENT '医疗类别(门诊/住院/急诊/购药)',
  `admission_date` date COMMENT '入院日期',
  `discharge_date` date COMMENT '出院日期',
  `total_amount` decimal(12,2) NOT NULL COMMENT '总费用',
  `diagnosis` varchar(512) COMMENT '诊断信息',
  `treatment_items` text COMMENT '诊疗项目明细(JSON格式)',
  `invoice_no` varchar(64) COMMENT '发票号',
  `report_time` datetime COMMENT '上报时间',
  `record_status` varchar(16) DEFAULT 'PENDING' COMMENT '记录状态(PENDING:待审核 SETTLED:已结算 CANCELLED:已取消)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_no` (`record_no`),
  KEY `idx_id_card` (`id_card`),
  KEY `idx_hospital_code` (`hospital_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='就医记录表';

-- =====================================================
-- 6. 结算记录表
-- =====================================================
DROP TABLE IF EXISTS `settlement_record`;
CREATE TABLE `settlement_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `settlement_no` varchar(32) NOT NULL COMMENT '结算单号',
  `record_no` varchar(32) NOT NULL COMMENT '就医记录编号',
  `id_card` varchar(32) NOT NULL COMMENT '身份证号',
  `name` varchar(64) NOT NULL COMMENT '姓名',
  `hospital_code` varchar(32) NOT NULL COMMENT '医院编码',
  `hospital_name` varchar(128) COMMENT '医院名称',
  `province_code` varchar(16) COMMENT '省份编码',
  `province_name` varchar(64) COMMENT '省份名称',
  `policy_code` varchar(32) COMMENT '政策编码',
  `medical_type` varchar(32) NOT NULL COMMENT '医疗类别',
  `total_amount` decimal(12,2) NOT NULL COMMENT '总费用',
  `self_pay_amount` decimal(12,2) DEFAULT 0 COMMENT '自费金额',
  `deductible_amount` decimal(12,2) DEFAULT 0 COMMENT '起付线金额',
  `within_scope_amount` decimal(12,2) DEFAULT 0 COMMENT '报销范围内金额',
  `reimbursement_amount` decimal(12,2) DEFAULT 0 COMMENT '报销总金额',
  `personal_account_pay` decimal(12,2) DEFAULT 0 COMMENT '个人账户支付',
  `unified_fund_pay` decimal(12,2) DEFAULT 0 COMMENT '统筹基金支付',
  `other_fund_pay` decimal(12,2) DEFAULT 0 COMMENT '其他基金支付',
  `cash_pay` decimal(12,2) DEFAULT 0 COMMENT '现金支付',
  `settlement_type` varchar(16) DEFAULT 'NORMAL' COMMENT '结算类型(NORMAL:正常结算 REVERSE:冲正结算)',
  `settlement_time` datetime COMMENT '结算时间',
  `settlement_status` varchar(16) DEFAULT 'SUCCESS' COMMENT '结算状态(0:待结算 1:结算成功 2:结算失败 3:已撤销 4:已冲正)',
  `operator` varchar(64) COMMENT '经办人',
  `remark` varchar(512) COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_settlement_no` (`settlement_no`),
  KEY `idx_record_no` (`record_no`),
  KEY `idx_id_card` (`id_card`),
  KEY `idx_settlement_time` (`settlement_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='结算记录表';

-- =====================================================
-- 7. 账户转移记录表
-- =====================================================
DROP TABLE IF EXISTS `account_transfer_record`;
CREATE TABLE `account_transfer_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `transfer_no` varchar(32) NOT NULL COMMENT '转移单号',
  `id_card` varchar(32) NOT NULL COMMENT '身份证号',
  `name` varchar(64) NOT NULL COMMENT '姓名',
  `personal_account_no` varchar(32) NOT NULL COMMENT '个人账户编号',
  `target_province_code` varchar(16) NOT NULL COMMENT '目标省份编码',
  `target_province_name` varchar(64) NOT NULL COMMENT '目标省份名称',
  `target_insurance_area` varchar(128) COMMENT '目标参保地区',
  `target_account_no` varchar(64) COMMENT '目标账户号',
  `transfer_amount` decimal(12,2) NOT NULL COMMENT '转移金额',
  `balance_before_transfer` decimal(12,2) DEFAULT 0 COMMENT '转移前余额',
  `balance_after_transfer` decimal(12,2) DEFAULT 0 COMMENT '转移后余额',
  `transfer_reason` varchar(512) COMMENT '转移原因',
  `apply_time` datetime COMMENT '申请时间',
  `audit_time` datetime COMMENT '审核时间',
  `auditor` varchar(64) COMMENT '审核人',
  `transfer_time` datetime COMMENT '转移完成时间',
  `transfer_status` varchar(16) DEFAULT '0' COMMENT '转移状态(0:待审核 1:审核通过 2:审核拒绝 3:转移中 4:转移成功 5:转移失败 6:已取消)',
  `external_transfer_no` varchar(64) COMMENT '外部转移流水号',
  `fail_reason` varchar(512) COMMENT '失败原因',
  `retry_count` int DEFAULT 0 COMMENT '重试次数',
  `remark` varchar(512) COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transfer_no` (`transfer_no`),
  KEY `idx_id_card` (`id_card`),
  KEY `idx_transfer_status` (`transfer_status`),
  KEY `idx_apply_time` (`apply_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账户转移记录表';

-- =====================================================
-- 8. 账户流水记录表
-- =====================================================
DROP TABLE IF EXISTS `account_flow_record`;
CREATE TABLE `account_flow_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `flow_no` varchar(32) NOT NULL COMMENT '流水号',
  `personal_account_no` varchar(32) NOT NULL COMMENT '个人账户编号',
  `id_card` varchar(32) NOT NULL COMMENT '身份证号',
  `flow_type` varchar(16) NOT NULL COMMENT '流水类型(1:收入 2:支出 3:转出 4:转入 5:冻结 6:解冻 7:调整)',
  `amount` decimal(12,2) NOT NULL COMMENT '变动金额',
  `balance_before` decimal(12,2) DEFAULT 0 COMMENT '变动前余额',
  `balance_after` decimal(12,2) DEFAULT 0 COMMENT '变动后余额',
  `business_type` varchar(32) COMMENT '业务类型(结算/转移/调账...)',
  `business_no` varchar(32) COMMENT '业务单号',
  `related_account` varchar(64) COMMENT '对方账户',
  `occur_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
  `operator` varchar(64) COMMENT '经办人',
  `remark` varchar(512) COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_flow_no` (`flow_no`),
  KEY `idx_personal_account_no` (`personal_account_no`),
  KEY `idx_id_card` (`id_card`),
  KEY `idx_occur_time` (`occur_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账户流水记录表';

-- =====================================================
-- 初始化数据
-- =====================================================

-- 初始化医保政策数据
INSERT INTO `medical_policy` (`policy_code`, `policy_name`, `insurance_type`, `treatment_type`, `deductible`, `reimbursement_ratio`, `max_reimbursement_amount`, `applicable_scope`, `effective_date`, `expiry_date`, `policy_status`, `description`) VALUES
('BJ-ZG-MZ-2024', '北京市职工医保门诊跨省报销政策', '1', '门诊', 1800.00, 70.00, 20000.00, '跨省定点医疗机构门诊', '2024-01-01', '2099-12-31', 'ACTIVE', '职工医保门诊跨省直接结算，起付线1800元，报销比例70%，年度最高支付2万元'),
('BJ-ZG-ZY-2024', '北京市职工医保住院跨省报销政策', '1', '住院', 1300.00, 85.00, 300000.00, '跨省定点医疗机构住院', '2024-01-01', '2099-12-31', 'ACTIVE', '职工医保住院跨省直接结算，起付线1300元，报销比例85%，年度最高支付30万元'),
('BJ-JM-MZ-2024', '北京市居民医保门诊跨省报销政策', '2', '门诊', 550.00, 50.00, 4000.00, '跨省定点医疗机构门诊', '2024-01-01', '2099-12-31', 'ACTIVE', '居民医保门诊跨省直接结算，起付线550元，报销比例50%，年度最高支付4000元'),
('BJ-JM-ZY-2024', '北京市居民医保住院跨省报销政策', '2', '住院', 1300.00, 75.00, 250000.00, '跨省定点医疗机构住院', '2024-01-01', '2099-12-31', 'ACTIVE', '居民医保住院跨省直接结算，起付线1300元，报销比例75%，年度最高支付25万元'),
('BJ-TX-MZ-2024', '北京市退休人员门诊跨省报销政策', '3', '门诊', 1300.00, 80.00, 20000.00, '跨省定点医疗机构门诊', '2024-01-01', '2099-12-31', 'ACTIVE', '退休人员门诊跨省直接结算，起付线1300元，报销比例80%，年度最高支付2万元'),
('BJ-TX-ZY-2024', '北京市退休人员住院跨省报销政策', '3', '住院', 1300.00, 90.00, 500000.00, '跨省定点医疗机构住院', '2024-01-01', '2099-12-31', 'ACTIVE', '退休人员住院跨省直接结算，起付线1300元，报销比例90%，年度最高支付50万元');

-- 初始化跨省定点医院数据
INSERT INTO `cross_province_hospital` (`hospital_code`, `hospital_name`, `province_code`, `province_name`, `city_code`, `city_name`, `hospital_level`, `hospital_type`, `contact_person`, `contact_phone`, `address`, `settlement_status`, `interface_url`, `app_id`, `app_secret`) VALUES
('HEB-SJZ-001', '河北省人民医院', '130000', '河北省', '130100', '石家庄市', '三级甲等', '综合医院', '张主任', '0311-85989696', '河北省石家庄市新华区和平西路348号', 'ACTIVE', 'http://127.0.0.1:8081/api/hospital', 'HEB001', 'secret001'),
('HAN-WH-001', '华中科技大学同济医学院附属同济医院', '420000', '湖北省', '420100', '武汉市', '三级甲等', '综合医院', '李主任', '027-83662688', '湖北省武汉市硚口区解放大道1095号', 'ACTIVE', 'http://127.0.0.1:8082/api/hospital', 'HUB001', 'secret002'),
('GUN-GZ-001', '广东省人民医院', '440000', '广东省', '440100', '广州市', '三级甲等', '综合医院', '王主任', '020-83827812', '广东省广州市越秀区中山二路106号', 'ACTIVE', 'http://127.0.0.1:8083/api/hospital', 'GUD001', 'secret003'),
('ZHE-HZ-001', '浙江大学医学院附属第一医院', '330000', '浙江省', '330100', '杭州市', '三级甲等', '综合医院', '陈主任', '0571-87236114', '浙江省杭州市上城区庆春路79号', 'ACTIVE', 'http://127.0.0.1:8084/api/hospital', 'ZHE001', 'secret004'),
('JIA-NJ-001', '江苏省人民医院', '320000', '江苏省', '320100', '南京市', '三级甲等', '综合医院', '刘主任', '025-83714511', '江苏省南京市鼓楼区广州路300号', 'ACTIVE', 'http://127.0.0.1:8085/api/hospital', 'JSU001', 'secret005');

-- 初始化测试参保人数据
INSERT INTO `insured_person` (`id_card`, `name`, `gender`, `birth_date`, `insurance_type`, `insurance_status`, `account_balance`, `cumulative_payment`, `work_unit`, `contact_phone`, `address`, `personal_account_no`) VALUES
('110101199001011234', '张三', '男', '1990-01-01', '1', '正常参保', 12580.50, 36000.00, '北京某科技有限公司', '13800138001', '北京市朝阳区建国路88号', 'BJ202401010000000001'),
('110101198505055678', '李四', '女', '1985-05-05', '1', '正常参保', 8650.00, 54000.00, '北京某教育培训机构', '13800138002', '北京市海淀区中关村大街1号', 'BJ202401010000000002'),
('110101196012129012', '王五', '男', '1960-12-12', '3', '正常参保', 28900.80, 120000.00, '已退休', '13800138003', '北京市西城区西长安街1号', 'BJ202401010000000003'),
('110101199508083456', '赵六', '女', '1995-08-08', '2', '正常参保', 3200.00, 8000.00, '北京某社区服务中心', '13800138004', '北京市丰台区南三环中路15号', 'BJ202401010000000004');

-- 初始化个人账户数据
INSERT INTO `personal_account` (`personal_account_no`, `id_card`, `balance`, `frozen_amount`, `available_balance`, `total_income`, `total_expense`, `last_settlement_date`, `account_status`) VALUES
('BJ202401010000000001', '110101199001011234', 12580.50, 0.00, 12580.50, 36000.00, 23419.50, '2024-06-15', 'NORMAL'),
('BJ202401010000000002', '110101198505055678', 8650.00, 0.00, 8650.00, 54000.00, 45350.00, '2024-06-10', 'NORMAL'),
('BJ202401010000000003', '110101196012129012', 28900.80, 0.00, 28900.80, 120000.00, 91099.20, '2024-06-18', 'NORMAL'),
('BJ202401010000000004', '110101199508083456', 3200.00, 0.00, 3200.00, 8000.00, 4800.00, '2024-05-20', 'NORMAL');

-- =====================================================
-- 9. 外省报销单据表
-- =====================================================
DROP TABLE IF EXISTS `external_reimbursement_bill`;
CREATE TABLE `external_reimbursement_bill` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `external_bill_no` varchar(64) NOT NULL COMMENT '外省单据号',
  `hospital_code` varchar(32) NOT NULL COMMENT '医院编码',
  `hospital_name` varchar(128) COMMENT '医院名称',
  `province_code` varchar(16) COMMENT '省份编码',
  `province_name` varchar(64) COMMENT '省份名称',
  `id_card` varchar(32) NOT NULL COMMENT '身份证号',
  `name` varchar(64) COMMENT '姓名',
  `medical_type` varchar(16) COMMENT '就医类型(门诊/住院)',
  `total_amount` decimal(12,2) DEFAULT 0 COMMENT '总费用',
  `reimbursement_amount` decimal(12,2) DEFAULT 0 COMMENT '医保报销金额',
  `personal_pay_amount` decimal(12,2) DEFAULT 0 COMMENT '个人支付金额',
  `account_pay_amount` decimal(12,2) DEFAULT 0 COMMENT '个账支付金额',
  `cash_pay_amount` decimal(12,2) DEFAULT 0 COMMENT '现金支付金额',
  `treatment_date` date COMMENT '就医日期',
  `discharge_date` date COMMENT '出院日期',
  `diagnosis` varchar(512) COMMENT '诊断',
  `invoice_no` varchar(64) COMMENT '发票号',
  `bill_status` varchar(16) DEFAULT 'IMPORTED' COMMENT '单据状态(IMPORTED:已导入 RECONCILED:已对账 EXCEPTION:异常)',
  `source_system` varchar(64) COMMENT '来源系统',
  `import_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '导入时间',
  `reconciliation_time` datetime COMMENT '对账时间',
  `reconciliation_batch_no` varchar(64) COMMENT '对账批次号',
  `remark` varchar(512) COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_external_bill_no` (`external_bill_no`),
  KEY `idx_hospital_code` (`hospital_code`),
  KEY `idx_id_card` (`id_card`),
  KEY `idx_treatment_date` (`treatment_date`),
  KEY `idx_bill_status` (`bill_status`),
  KEY `idx_reconciliation_batch_no` (`reconciliation_batch_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='外省报销单据表';

-- =====================================================
-- 10. 对账批次表
-- =====================================================
DROP TABLE IF EXISTS `reconciliation_batch`;
CREATE TABLE `reconciliation_batch` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `batch_no` varchar(64) NOT NULL COMMENT '对账批次号',
  `reconciliation_date` date NOT NULL COMMENT '对账日期',
  `province_code` varchar(16) COMMENT '对账省份编码(为空则对账所有省份)',
  `province_name` varchar(64) COMMENT '对账省份名称',
  `total_bills` int DEFAULT 0 COMMENT '单据总数',
  `matched_bills` int DEFAULT 0 COMMENT '对账一致数',
  `mismatched_bills` int DEFAULT 0 COMMENT '对账不一致数',
  `missing_local_bills` int DEFAULT 0 COMMENT '本地缺失单据数',
  `missing_external_bills` int DEFAULT 0 COMMENT '外省缺失单据数',
  `total_local_amount` decimal(15,2) DEFAULT 0 COMMENT '本地总金额',
  `total_external_amount` decimal(15,2) DEFAULT 0 COMMENT '外省总金额',
  `diff_amount` decimal(15,2) DEFAULT 0 COMMENT '差额',
  `batch_status` varchar(16) DEFAULT 'PROCESSING' COMMENT '批次状态(PROCESSING:处理中 COMPLETED:已完成 FAILED:失败)',
  `start_time` datetime COMMENT '开始时间',
  `end_time` datetime COMMENT '结束时间',
  `operator` varchar(64) COMMENT '操作人',
  `remark` varchar(512) COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_batch_no` (`batch_no`),
  KEY `idx_reconciliation_date` (`reconciliation_date`),
  KEY `idx_province_code` (`province_code`),
  KEY `idx_batch_status` (`batch_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='对账批次表';

-- =====================================================
-- 11. 对账明细表
-- =====================================================
DROP TABLE IF EXISTS `reconciliation_detail`;
CREATE TABLE `reconciliation_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `batch_no` varchar(64) NOT NULL COMMENT '对账批次号',
  `external_bill_no` varchar(64) COMMENT '外省单据号',
  `local_settlement_no` varchar(64) COMMENT '本地结算单号',
  `hospital_code` varchar(32) COMMENT '医院编码',
  `id_card` varchar(32) COMMENT '身份证号',
  `name` varchar(64) COMMENT '姓名',
  `medical_type` varchar(16) COMMENT '就医类型',
  `treatment_date` date COMMENT '就医日期',
  `local_total_amount` decimal(12,2) DEFAULT 0 COMMENT '本地总金额',
  `local_reimbursement_amount` decimal(12,2) DEFAULT 0 COMMENT '本地报销金额',
  `local_personal_pay` decimal(12,2) DEFAULT 0 COMMENT '本地个人支付',
  `external_total_amount` decimal(12,2) DEFAULT 0 COMMENT '外省总金额',
  `external_reimbursement_amount` decimal(12,2) DEFAULT 0 COMMENT '外省报销金额',
  `external_personal_pay` decimal(12,2) DEFAULT 0 COMMENT '外省个人支付',
  `total_diff` decimal(12,2) DEFAULT 0 COMMENT '总金额差额',
  `reimbursement_diff` decimal(12,2) DEFAULT 0 COMMENT '报销金额差额',
  `personal_pay_diff` decimal(12,2) DEFAULT 0 COMMENT '个人支付差额',
  `match_status` varchar(16) DEFAULT 'MATCHED' COMMENT '匹配状态(MATCHED:一致 MISMATCHED:不一致 MISSING_LOCAL:本地缺失 MISSING_EXTERNAL:外省缺失)',
  `reconciliation_result` varchar(1024) COMMENT '对账结果说明',
  `is_exception` tinyint DEFAULT 0 COMMENT '是否异常(0:否 1:是)',
  `exception_handled` tinyint DEFAULT 0 COMMENT '异常是否已处理(0:否 1:是)',
  `handler` varchar(64) COMMENT '处理人',
  `handle_time` datetime COMMENT '处理时间',
  `handle_remark` varchar(512) COMMENT '处理备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_external_bill_no` (`external_bill_no`),
  KEY `idx_local_settlement_no` (`local_settlement_no`),
  KEY `idx_id_card` (`id_card`),
  KEY `idx_match_status` (`match_status`),
  KEY `idx_is_exception` (`is_exception`),
  KEY `idx_exception_handled` (`exception_handled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='对账明细表';

-- =====================================================
-- 12. 对账异常表
-- =====================================================
DROP TABLE IF EXISTS `reconciliation_exception`;
CREATE TABLE `reconciliation_exception` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `exception_no` varchar(64) NOT NULL COMMENT '异常编号',
  `batch_no` varchar(64) NOT NULL COMMENT '对账批次号',
  `reconciliation_detail_id` bigint COMMENT '对账明细ID',
  `external_bill_no` varchar(64) COMMENT '外省单据号',
  `local_settlement_no` varchar(64) COMMENT '本地结算单号',
  `hospital_code` varchar(32) COMMENT '医院编码',
  `hospital_name` varchar(128) COMMENT '医院名称',
  `province_code` varchar(16) COMMENT '省份编码',
  `province_name` varchar(64) COMMENT '省份名称',
  `id_card` varchar(32) COMMENT '身份证号',
  `name` varchar(64) COMMENT '姓名',
  `medical_type` varchar(16) COMMENT '就医类型',
  `treatment_date` date COMMENT '就医日期',
  `exception_type` varchar(32) COMMENT '异常类型(AMOUNT_DIFF:金额不符 MISSING_LOCAL:本地缺失 MISSING_EXTERNAL:外省缺失 DUPLICATE:重复单据)',
  `local_total_amount` decimal(12,2) DEFAULT 0 COMMENT '本地总金额',
  `local_reimbursement_amount` decimal(12,2) DEFAULT 0 COMMENT '本地报销金额',
  `external_total_amount` decimal(12,2) DEFAULT 0 COMMENT '外省总金额',
  `external_reimbursement_amount` decimal(12,2) DEFAULT 0 COMMENT '外省报销金额',
  `total_diff` decimal(12,2) DEFAULT 0 COMMENT '总金额差额',
  `reimbursement_diff` decimal(12,2) DEFAULT 0 COMMENT '报销金额差额',
  `exception_desc` varchar(1024) COMMENT '异常描述',
  `exception_status` varchar(16) DEFAULT 'PENDING' COMMENT '异常状态(PENDING:待处理 PROCESSING:处理中 RESOLVED:已处理 CLOSED:已关闭)',
  `handler` varchar(64) COMMENT '处理人',
  `handle_time` datetime COMMENT '处理时间',
  `handle_method` varchar(32) COMMENT '处理方式(ADJUST:调账 REFUND:退款 WRITE_OFF:核销 OTHER:其他)',
  `handle_remark` varchar(512) COMMENT '处理备注',
  `follow_up_person` varchar(64) COMMENT '跟进人',
  `next_follow_up_date` date COMMENT '下次跟进日期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exception_no` (`exception_no`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_external_bill_no` (`external_bill_no`),
  KEY `idx_local_settlement_no` (`local_settlement_no`),
  KEY `idx_id_card` (`id_card`),
  KEY `idx_exception_type` (`exception_type`),
  KEY `idx_exception_status` (`exception_status`),
  KEY `idx_handler` (`handler`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='对账异常表';
