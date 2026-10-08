package com.example.demo.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Bill {
    private Integer billId;       // bill_id
    private Integer reserveId;    // reserve_id
    private String petName;       // 关联预约的宠物名（join，非表字段）
    private String serviceName;   // 关联预约的服务名（join，非表字段）
    private String username;      // 关联预约的用户名（join，非表字段）
    private BigDecimal totalAmount; // total_amount
    private Integer pointsUsed;   // points_used
    private BigDecimal payAmount; // pay_amount
    private String payMethod;     // pay_method 线上/线下
    private String payStatus;
    private Integer serviceMinutes; // 实际服务分钟数（联表计算，非表字段）     // pay_status 未支付/已支付
    private String createTime;    // create_time
}
