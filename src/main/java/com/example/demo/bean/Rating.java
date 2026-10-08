package com.example.demo.bean;

import lombok.Data;

@Data
public class Rating {
    private Integer ratingId;     // rating_id
    private Integer reserveId;    // reserve_id
    private Integer staffId;      // staff_id
    private String staffName;     // 店员名（join，非表字段）
    private String username;      // username
    private Integer score;        // score 1-5
    private String comment;       // comment
    private String createTime;    // create_time
}
