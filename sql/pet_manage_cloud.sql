-- ============================================================
-- 宠物管理系统 数据库脚本 pet_manage
-- 数据库版本：MySQL 5.7（云端导入版：无建库语句，直接导入现有库）
-- 说明：本脚本按本机 pet_manage 库的实际表结构与数据生成，
--       包含建库、建表、约束、索引及基础测试数据，可直接整体执行。
-- ============================================================


-- ------------------------------------------------------------
-- 1. 用户表 user（实体：用户，属性：用户ID/用户名/登录密码/角色/手机号）
--    手机号唯一约束：注册时未填手机号应传 NULL（NULL 允许多个）
-- ------------------------------------------------------------
CREATE TABLE `user` (
  `user_id`  int(11)     NOT NULL AUTO_INCREMENT COMMENT '用户主键id',
  `username` varchar(20) NOT NULL                COMMENT '用户名',
  `password` varchar(50) NOT NULL                COMMENT '密码',
  `role`     varchar(10) NOT NULL                COMMENT '角色：管理员/宠物主人',
  `phone`    varchar(11)          DEFAULT NULL   COMMENT '手机号',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ------------------------------------------------------------
-- 2. 主人表 owner（实体：主人，属性：主人编号/真实姓名/联系电话/居住地址/用户ID）
--    用户-主人 1:1，user_id 为外键；李四无关联账号，故允许为空
-- ------------------------------------------------------------
CREATE TABLE `owner` (
  `owner_id`  int(11)     NOT NULL AUTO_INCREMENT COMMENT '主人编号',
  `real_name` varchar(10) NOT NULL                COMMENT '真实姓名',
  `phone`     varchar(11) NOT NULL                COMMENT '联系电话',
  `address`   varchar(100)         DEFAULT NULL   COMMENT '居住地址',
  `user_id`   int(11)              DEFAULT NULL   COMMENT '关联用户ID',
  PRIMARY KEY (`owner_id`),
  KEY `idx_owner_user` (`user_id`),
  CONSTRAINT `fk_owner_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物主人表';

-- ------------------------------------------------------------
-- 3. 宠物表 pet（实体：宠物，属性：宠物编号/宠物名/品种/年龄/性别/健康状况/主人编号）
--    主人-宠物 1:n，owner_id 为外键
-- ------------------------------------------------------------
CREATE TABLE `pet` (
  `pet_id`   int(11)     NOT NULL AUTO_INCREMENT COMMENT '宠物编号',
  `pet_name` varchar(20) NOT NULL                COMMENT '宠物名',
  `variety`  varchar(20)          DEFAULT NULL   COMMENT '品种',
  `age`      int(11)              DEFAULT NULL   COMMENT '年龄',
  `gender`   varchar(4)           DEFAULT NULL   COMMENT '性别',
  `health`   varchar(50)          DEFAULT NULL   COMMENT '健康状况',
  `owner_id` int(11)     NOT NULL                COMMENT '所属主人编号',
  PRIMARY KEY (`pet_id`),
  KEY `idx_pet_owner` (`owner_id`),
  CONSTRAINT `fk_pet_owner` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物表';

-- ------------------------------------------------------------
-- 4. 服务项目表 pet_service（实体：服务，属性：服务编号/服务名称/服务价格/服务描述）
--    金额用 decimal 保证精度
-- ------------------------------------------------------------
CREATE TABLE `pet_service` (
  `id`           int(11)      NOT NULL AUTO_INCREMENT COMMENT '服务id',
  `service_name` varchar(100) NOT NULL                COMMENT '服务项目名称',
  `price`        decimal(10,2) NOT NULL               COMMENT '服务价格',
  `description`  varchar(500)          DEFAULT NULL   COMMENT '服务描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务项目表';

-- ------------------------------------------------------------
-- 5. 店员表 staff（实体：店员，属性：店员编号/姓名/电话/擅长/状态/平均评分）
--    顾客可查看店员空闲情况并选择；服务完成后可对店员评分
-- ------------------------------------------------------------
CREATE TABLE `staff` (
  `staff_id`   int(11)      NOT NULL AUTO_INCREMENT COMMENT '店员编号',
  `staff_name` varchar(20)  NOT NULL                COMMENT '店员姓名',
  `phone`      varchar(11)           DEFAULT NULL   COMMENT '联系电话',
  `skill`      varchar(100)          DEFAULT NULL   COMMENT '擅长项目',
  `status`     varchar(10)  NOT NULL DEFAULT '在职' COMMENT '在职/离职',
  `avg_score`  decimal(2,1) NOT NULL DEFAULT 0.0   COMMENT '平均评分',
  PRIMARY KEY (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店员表';

-- ------------------------------------------------------------
-- 5.1 预约记录表 reserve（实体：预约记录）
--    字段：预约编号/宠物名字/预约服务/预约人/预约日期/预约时段/店员ID/状态
--    状态流转：待审核 → 已同意 → 进行中 → 已完成（或 已拒绝/已取消）
-- ------------------------------------------------------------
CREATE TABLE `reserve` (
  `id`           int(11)      NOT NULL AUTO_INCREMENT COMMENT '预约id',
  `pet_name`     varchar(50)  NOT NULL                COMMENT '宠物名字',
  `service_name` varchar(50)  NOT NULL                COMMENT '预约服务名称',
  `username`     varchar(20)  NOT NULL                COMMENT '预约提交人(用户名)',
  `reserve_date` date                  DEFAULT NULL   COMMENT '预约日期',
  `reserve_time` varchar(20)           DEFAULT NULL   COMMENT '预约时段 如 09:00-10:00',
  `pet_id`       int(11)               DEFAULT NULL   COMMENT '预约宠物ID(归属校验)',
  `staff_id`     int(11)               DEFAULT NULL   COMMENT '选择店员',
  `price`        decimal(10,2) NOT NULL DEFAULT 0.00  COMMENT '预约时服务价格快照',
  `status`       varchar(20)  NOT NULL DEFAULT '待审核' COMMENT '状态：待审核/已同意/进行中/已完成/已拒绝/已取消',
  PRIMARY KEY (`id`),
  KEY `idx_reserve_staff` (`staff_id`),
  KEY `idx_reserve_pet` (`pet_id`),
  CONSTRAINT `fk_reserve_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `fk_reserve_pet`   FOREIGN KEY (`pet_id`)   REFERENCES `pet` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约记录表';

-- ------------------------------------------------------------
-- 5.2 会员表 member（实体：会员，属性：会员编号/关联用户/会员等级/积分/开通时间）
--    管理员可开通会员；1元消费=1积分，1积分=1元抵扣
-- ------------------------------------------------------------
CREATE TABLE `member` (
  `member_id`   int(11)     NOT NULL AUTO_INCREMENT COMMENT '会员编号',
  `user_id`     int(11)     NOT NULL                COMMENT '关联用户',
  `level`       varchar(20) NOT NULL DEFAULT '普通会员' COMMENT '会员等级',
  `points`      int(11)     NOT NULL DEFAULT 0      COMMENT '积分(1元=1积分)',
  `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开通时间',
  PRIMARY KEY (`member_id`),
  KEY `idx_member_user` (`user_id`),
  CONSTRAINT `fk_member_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- ------------------------------------------------------------
-- 5.3 账单表 bill（实体：账单，属性：账单编号/关联预约/总金额/使用积分/实付金额/支付方式/支付状态）
--    服务完成生成账单；会员可用积分抵扣，线上支付为模拟支付
-- ------------------------------------------------------------
CREATE TABLE `bill` (
  `bill_id`      int(11)      NOT NULL AUTO_INCREMENT COMMENT '账单编号',
  `reserve_id`   int(11)      NOT NULL                COMMENT '关联预约',
  `total_amount` decimal(10,2) NOT NULL DEFAULT 0.00  COMMENT '项目总金额',
  `points_used`  int(11)      NOT NULL DEFAULT 0      COMMENT '使用积分',
  `pay_amount`   decimal(10,2) NOT NULL DEFAULT 0.00  COMMENT '实际支付金额',
  `pay_method`   varchar(10)           DEFAULT NULL   COMMENT '线上/线下',
  `pay_status`   varchar(10)  NOT NULL DEFAULT '未支付' COMMENT '未支付/已支付',
  `create_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  PRIMARY KEY (`bill_id`),
  KEY `idx_bill_reserve` (`reserve_id`),
  CONSTRAINT `fk_bill_reserve` FOREIGN KEY (`reserve_id`) REFERENCES `reserve` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';

-- ------------------------------------------------------------
-- 5.4 评分表 rating（实体：评分，属性：评分编号/关联预约/店员/评价人/分数/评价内容）
-- ------------------------------------------------------------
CREATE TABLE `rating` (
  `rating_id`   int(11)      NOT NULL AUTO_INCREMENT COMMENT '评分编号',
  `reserve_id`  int(11)      NOT NULL                COMMENT '关联预约',
  `staff_id`    int(11)      NOT NULL                COMMENT '被评店员',
  `username`    varchar(20)           DEFAULT NULL   COMMENT '评价人',
  `score`       int(11)      NOT NULL DEFAULT 5      COMMENT '评分 1-5',
  `comment`     varchar(200)          DEFAULT NULL   COMMENT '评价内容',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`rating_id`),
  KEY `idx_rating_reserve` (`reserve_id`),
  KEY `idx_rating_staff` (`staff_id`),
  CONSTRAINT `fk_rating_reserve` FOREIGN KEY (`reserve_id`) REFERENCES `reserve` (`id`),
  CONSTRAINT `fk_rating_staff`   FOREIGN KEY (`staff_id`)   REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评分表';

-- ------------------------------------------------------------
-- 6. 公告表 notice（实体：公告，属性：公告编号/标题/内容/发布时间）
-- ------------------------------------------------------------
CREATE TABLE `notice` (
  `id`          int(11)      NOT NULL AUTO_INCREMENT COMMENT '公告主键',
  `title`       varchar(100) NOT NULL                COMMENT '公告标题',
  `content`     varchar(500) NOT NULL                COMMENT '公告内容',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

-- ------------------------------------------------------------
-- 7. 留言表 message（实体：留言，属性：留言编号/留言人/留言内容/留言时间）
-- ------------------------------------------------------------
CREATE TABLE `message` (
  `id`          int(11)      NOT NULL AUTO_INCREMENT COMMENT '留言id',
  `username`    varchar(50)  NOT NULL                COMMENT '留言人',
  `content`     varchar(500) NOT NULL                COMMENT '留言内容',
  `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '留言时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户留言表';

-- ============================================================
-- 基础测试数据（与本机库截图内容一致）
-- ============================================================

-- 用户数据（xixi 未填手机号，使用 NULL 以兼容手机号唯一约束）
INSERT INTO `user` (`user_id`, `username`, `password`, `role`, `phone`) VALUES
(1, 'admin', '123456', '管理员',   '13800138000'),
(2, 'user1', '123456', '宠物主人', '13900139000'),
(5, 'xixi',  '654321', '宠物主人', NULL);

-- 主人数据（张三关联 user1；李四无关联账号，user_id 为空）
INSERT INTO `owner` (`owner_id`, `real_name`, `phone`, `address`, `user_id`) VALUES
(1, '张三', '13900139000', '烟台市XX小区', 2),
(2, '李四', '12345666',    '花园小区',     NULL);
-- 注：原库中宠物 7/8 的 owner_id=3/5 在主人表中不存在（历史孤儿数据），
--     为满足外键参照完整性，此处补充两条模拟主人记录，可按需修改。
INSERT INTO `owner` (`owner_id`, `real_name`, `phone`, `address`, `user_id`) VALUES
(3, '模拟主人A', '13700000003', '示例地址3', NULL),
(5, '模拟主人B', '13700000005', '示例地址5', NULL);

-- 宠物数据
INSERT INTO `pet` (`pet_id`, `pet_name`, `variety`, `age`, `gender`, `health`, `owner_id`) VALUES
(1, '旺旺',   '金毛',     4, '公', '良好', 1),
(7, '张啵啵', '拉布拉多', 2, '女', '良好', 3),
(8, '花花',   '边牧',     4, '男', '良好', 5);

-- 服务项目数据
INSERT INTO `pet_service` (`id`, `service_name`, `price`, `description`) VALUES
(1, '宠物洗澡', 88.00,  '宠物清洁洗浴护理'),
(2, '宠物剪毛', 120.00, '宠物修剪毛发造型'),
(3, '宠物驱虫', 65.00,  '体内外驱虫服务');

-- 店员数据
INSERT INTO `staff` (`staff_id`, `staff_name`, `phone`, `skill`, `status`, `avg_score`) VALUES
(1, '阿伟', '13711110001', '宠物洗澡、剪毛',   '在职', 4.8),
(2, '小美', '13711110002', '美容造型、驱虫',   '在职', 4.9),
(3, '大壮', '13711110003', '寄养照护、医疗协助', '在职', 4.5);

-- 会员数据（user1 为会员，初始积分 100）
INSERT INTO `member` (`member_id`, `user_id`, `level`, `points`, `create_time`) VALUES
(1, 2, '普通会员', 100, '2026-09-15 10:00:00');

-- 预约记录数据（预约必须关联自己的宠物 pet_id，防止用他人宠物预约）
INSERT INTO `reserve` (`id`, `pet_id`, `pet_name`, `service_name`, `username`, `reserve_date`, `reserve_time`, `staff_id`, `price`, `status`) VALUES
(1, 1, '旺旺', '宠物洗澡', 'user1', '2026-09-17', '14:00-15:00', 3, 88.00, '已完成');

-- 公告数据
INSERT INTO `notice` (`id`, `title`, `content`, `create_time`) VALUES
(1, '开业大酬宾', '新客洗澡9.9起', '2026-09-14 18:07:20');

-- 留言数据
INSERT INTO `message` (`id`, `username`, `content`, `create_time`) VALUES
(1, 'admin', '系统已上线，欢迎使用！',     '2026-09-14 17:45:46'),
(2, 'user1', '我的狗狗洗澡很干净，好评！', '2026-09-14 17:45:46');
