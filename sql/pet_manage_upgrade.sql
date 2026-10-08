-- =====================================================
-- 宠物管理系统 功能升级脚本 v2
-- 新增：店员表 staff / 会员表 member / 账单表 bill / 评分表 rating
-- 升级：预约表 reserve 增加 预约日期、预约时段、店员ID，状态扩展
-- MySQL 5.7 / pet_manage 库 / 不丢原数据
-- =====================================================
USE pet_manage;

-- ---------- 1. 店员表（新表，无历史数据） ----------
DROP TABLE IF EXISTS staff;
CREATE TABLE staff (
  staff_id   INT PRIMARY KEY AUTO_INCREMENT,
  staff_name VARCHAR(20)  NOT NULL COMMENT '店员姓名',
  phone      VARCHAR(11)  DEFAULT NULL COMMENT '联系电话',
  skill      VARCHAR(100) DEFAULT NULL COMMENT '擅长项目',
  status     VARCHAR(10)  DEFAULT '在职' COMMENT '在职/离职',
  avg_score  DECIMAL(2,1) DEFAULT 0.0 COMMENT '平均评分'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店员表';

INSERT INTO staff(staff_name, phone, skill, status, avg_score) VALUES
('阿伟','13711110001','宠物洗澡、剪毛','在职',4.8),
('小美','13711110002','美容造型、驱虫','在职',4.9),
('大壮','13711110003','寄养照护、医疗协助','在职',4.5);

-- ---------- 2. 预约表升级（加字段，不删表） ----------
ALTER TABLE reserve
  ADD COLUMN reserve_date DATE DEFAULT NULL COMMENT '预约日期',
  ADD COLUMN reserve_time VARCHAR(20) DEFAULT NULL COMMENT '预约时段 如 09:00-10:00',
  ADD COLUMN staff_id INT DEFAULT NULL COMMENT '选择店员';

ALTER TABLE reserve
  ADD CONSTRAINT fk_reserve_staff FOREIGN KEY (staff_id) REFERENCES staff(staff_id);

-- 给原有两条预约数据补上日期/时段/店员
UPDATE reserve SET reserve_date='2026-09-16', reserve_time='09:00-10:00', staff_id=1 WHERE id=1;
UPDATE reserve SET reserve_date='2026-09-16', reserve_time='10:00-11:00', staff_id=2 WHERE id=2;

-- ---------- 3. 会员表（新表） ----------
DROP TABLE IF EXISTS member;
CREATE TABLE member (
  member_id   INT PRIMARY KEY AUTO_INCREMENT,
  user_id     INT NOT NULL COMMENT '关联用户',
  level       VARCHAR(20) DEFAULT '普通会员' COMMENT '会员等级',
  points      INT         DEFAULT 0 COMMENT '积分(1元=1积分)',
  create_time DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '开通时间',
  CONSTRAINT fk_member_user FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- user1(主人账号) 开通为会员，初始积分 100
INSERT INTO member(user_id, level, points) VALUES (2, '普通会员', 100);

-- ---------- 4. 账单表（新表） ----------
DROP TABLE IF EXISTS bill;
CREATE TABLE bill (
  bill_id      INT PRIMARY KEY AUTO_INCREMENT,
  reserve_id   INT NOT NULL COMMENT '关联预约',
  total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '项目总金额',
  points_used  INT           DEFAULT 0 COMMENT '使用积分',
  pay_amount   DECIMAL(10,2) DEFAULT 0.00 COMMENT '实际支付金额',
  pay_method   VARCHAR(10)   DEFAULT NULL COMMENT '线上/线下',
  pay_status   VARCHAR(10)   DEFAULT '未支付' COMMENT '未支付/已支付',
  create_time  DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  CONSTRAINT fk_bill_reserve FOREIGN KEY (reserve_id) REFERENCES reserve(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';

-- ---------- 5. 评分表（新表） ----------
DROP TABLE IF EXISTS rating;
CREATE TABLE rating (
  rating_id   INT PRIMARY KEY AUTO_INCREMENT,
  reserve_id  INT NOT NULL COMMENT '关联预约',
  staff_id    INT NOT NULL COMMENT '被评店员',
  username    VARCHAR(20) DEFAULT NULL COMMENT '评价人',
  score       INT         DEFAULT 5 COMMENT '评分 1-5',
  comment     VARCHAR(200) DEFAULT NULL COMMENT '评价内容',
  create_time DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  CONSTRAINT fk_rating_reserve FOREIGN KEY (reserve_id) REFERENCES reserve(id),
  CONSTRAINT fk_rating_staff   FOREIGN KEY (staff_id)   REFERENCES staff(staff_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评分表';

-- ---------- 6. 校验 ----------
SELECT 'staff' AS t, COUNT(*) AS cnt FROM staff
UNION ALL SELECT 'reserve', COUNT(*) FROM reserve
UNION ALL SELECT 'member', COUNT(*) FROM member
UNION ALL SELECT 'bill', COUNT(*) FROM bill
UNION ALL SELECT 'rating', COUNT(*) FROM rating;
