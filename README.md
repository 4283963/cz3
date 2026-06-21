# 北京医保跨省异地就医结算系统

## 项目简介

本项目是北京医保跨省异地就医直接结算和个人账户转移的后端系统，采用Spring Boot + MyBatis Plus架构，由多个微服务模块组成。

## 项目结构

```
bj-medical-insurance/
├── common/                    # 公共模块
│   └── src/main/java/com/bjyb/common/
│       ├── entity/           # 实体类
│       ├── dto/              # 数据传输对象
│       ├── enums/            # 枚举类
│       ├── mapper/           # Mapper接口
│       ├── utils/            # 工具类
│       ├── exception/        # 异常处理
│       └── config/         # 配置类
├── settlement-service/        # 异地结算服务（端口: 8080）
│   └── src/main/java/com/bjyb/settlement/
│       ├── SettlementApplication.java    # 启动类
│       ├── controller/     # 控制层
│       └── service/        # 业务逻辑层
├── transfer-service/          # 个账转移服务（端口: 8081）
│   └── src/main/java/com/bjyb/transfer/
│       ├── TransferApplication.java      # 启动类
│       ├── controller/     # 控制层
│       ├── service/        # 业务逻辑层
│       └── config/         # 配置类
├── sql/                     # 数据库脚本
│   └── init.sql             # 数据库初始化脚本
└── pom.xml                  # 父POM
```

## 技术栈

- **框架**: Spring Boot 2.7.18
- **ORM**: MyBatis Plus 3.5.3.1
- **数据库**: MySQL 8.0+
- **连接池**: Druid 1.2.16
- **工具库**: Hutool 5.8.20
- **Java版本**: JDK 1.8

## 核心功能

### 1. 异地结算服务 (settlement-service)

接收外省定点医院传来的就医报销请求，根据北京医保政策计算报销金额并扣减个账。

**主要接口**:

| 接口 | 方法 | 说明 |
|--------|------|------|
| `/api/settlement/calculate` | POST | 异地就医结算 |
| `/api/settlement/{settlementNo}` | GET | 查询结算记录详情 |
| `/api/settlement/person/{idCard}` | GET | 查询参保人结算记录 |
| `/api/settlement/person/{idCard}/page` | GET | 分页查询参保人结算记录 |
| `/api/settlement/hospital/{hospitalCode}/page` | GET | 分页查询医院结算记录 |
| `/api/settlement/person/{idCard}/time-range` | GET | 按时间范围查询 |
| `/api/settlement/reverse/{settlementNo}` | POST | 结算冲正 |

**报销计算规则**:
1. 校验参保人状态和医院资质
2. 根据医保类型（职工/居民/退休）和就医类型（门诊/住院）匹配政策
3. 计算自费金额、起付线、报销范围内金额
4. 按报销比例计算报销金额，不超过年度最高支付限额
5. 优先从个人账户支付自付部分，余额不足时现金支付
6. 生成结算记录和账户流水

### 2. 个账转移服务 (transfer-service)

负责把参保人北京的个账余额同步到新参保省份。

**主要接口**:

| 接口 | 方法 | 说明 |
|--------|------|------|
| `/api/transfer/apply` | POST | 提交个账转移申请 |
| `/api/transfer/audit/{transferNo}` | POST | 审核转移申请 |
| `/api/transfer/retry/{transferNo}` | POST | 重试转移 |
| `/api/transfer/cancel/{transferNo}` | POST | 取消转移申请 |
| `/api/transfer/{transferNo}` | GET | 查询转移记录详情 |
| `/api/transfer/person/{idCard}` | GET | 查询参保人转移记录 |
| `/api/transfer/pending-audit` | GET | 查询待审核列表 |
| `/api/transfer/failed` | GET | 查询转移失败列表 |
| `/api/transfer/balance-sync` | POST | 接收外省份余额同步 |
| `/api/transfer/account/balance/{idCard}` | GET | 查询账户余额 |
| `/api/transfer/account/{accountNo}` | GET | 查询账户信息 |

**转移流程**:
1. 提交转移申请 → 2. 审核转移申请 → 3. 扣减个账余额 → 4. 同步到目标省份 → 5. 生成转移记录和流水

## 医保政策

系统内置6套北京医保跨省报销政策：

| 政策编码 | 医保类型 | 就医类型 | 起付线 | 报销比例 | 年度最高支付 |
|----------|----------|----------|--------|----------|------------|
| BJ-ZG-MZ-2024 | 职工医保 | 门诊 | 1800元 | 70% | 2万元 |
| BJ-ZG-ZY-2024 | 职工医保 | 住院 | 1300元 | 85% | 30万元 |
| BJ-JM-MZ-2024 | 居民医保 | 门诊 | 550元 | 50% | 4000元 |
| BJ-JM-ZY-2024 | 居民医保 | 住院 | 1300元 | 75% | 25万元 |
| BJ-TX-MZ-2024 | 退休人员 | 门诊 | 1300元 | 80% | 2万元 |
| BJ-TX-ZY-2024 | 退休人员 | 住院 | 1300元 | 90% | 50万元 |

## 数据库设计

### 核心数据表：

1. **insured_person** - 参保人信息表
2. **personal_account** - 个人账户表
3. **medical_policy** - 医保政策表
4. **cross_province_hospital** - 跨省定点医院表
5. **medical_record** - 就医记录表
6. **settlement_record** - 结算记录表
7. **account_transfer_record** - 账户转移记录表
8. **account_flow_record** - 账户流水记录表

## 快速开始

### 1. 数据库初始化

```bash
# 创建数据库并执行初始化脚本
mysql -u root -p < sql/init.sql
```

### 2. 编译项目

```bash
# 编译整个项目
mvn clean install -DskipTests
```

### 3. 启动服务

```bash
# 启动异地结算服务
cd settlement-service
mvn spring-boot:run

# 启动个账转移服务
cd transfer-service
mvn spring-boot:run
```

## API 调用示例

### 异地结算请求示例：

```bash
curl -X POST http://localhost:8080/settlement-service/api/settlement/calculate \
-H "Content-Type: application/json" \
-d '{
    "requestNo": "REQ20240621001",
    "hospitalCode": "HEB-SJZ-001",
    "hospitalName": "河北省人民医院",
    "idCard": "110101199001011234",
    "name": "张三",
    "medicalType": "门诊",
    "totalAmount": 5000.00,
    "diagnosis": "上呼吸道感染",
    "treatmentItems": [
        {
            "itemCode": "XM001",
            "itemName": "血常规检查",
            "itemType": "检查",
            "amount": 50.00,
            "withinScope": "Y"
        },
        {
            "itemCode": "XM002",
            "itemName": "阿莫西林胶囊",
            "itemType": "药品",
            "amount": 120.00,
            "withinScope": "Y"
        }
    ]
}'
```

### 个账转移申请示例：

```bash
curl -X POST http://localhost:8081/transfer-service/api/transfer/apply \
-H "Content-Type: application/json" \
-d '{
    "requestNo": "TR20240621001",
    "idCard": "110101199001011234",
    "name": "张三",
    "targetProvinceCode": "310000",
    "targetProvinceName": "上海市",
    "targetInsuranceArea": "上海市浦东新区",
    "targetAccountNo": "SH2024000001",
    "transferAmount": 5000.00,
    "transferReason": "工作调动，医保关系转移",
    "operator": "admin"
}'
```

## 测试数据

系统预置4名测试参保人：

| 身份证号 | 姓名 | 医保类型 | 账户余额 |
|----------|------|----------|----------|
| 110101199001011234 | 张三 | 职工医保 | 12580.50 |
| 110101198505055678 | 李四 | 职工医保 | 8650.00 |
| 110101196012129012 | 王五 | 退休人员 | 28900.80 |
| 110101199508083456 | 赵六 | 居民医保 | 3200.00 |

系统预置5家跨省定点医院：河北省人民医院、武汉同济医院、广东省人民医院、浙大一院、江苏省人民医院。

## 核心类说明

### ReimbursementCalculationService

报销计算核心服务，实现：
- 参保人信息和医院资质校验
- 医保政策匹配和报销金额计算
- 个人账户扣款和流水记录
- 就医记录和结算记录生成

### AccountTransferService

个账转移核心服务，实现：
- 转移申请提交和审核
- 账户余额扣减和同步
- 转移状态跟踪和重试机制
- 失败回滚和账户流水记录

### BalanceSyncService

余额同步服务，实现：
- 接收外省份余额同步请求
- 账户余额调整和流水记录
- 自动创建新账户
