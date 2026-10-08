package com.example.demo.bean;

import lombok.Data;

@Data
public class Member {
    private Integer memberId;     // member_id
    private Integer userId;       // user_id
    private String username;      // 关联用户名（join user 查出，非表字段）
    private String level;         // 会员等级
    private Integer points;       // 积分
    private String createTime;    // create_time
}
