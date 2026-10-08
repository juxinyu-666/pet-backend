package com.example.demo.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Staff {
    private Integer staffId;      // staff_id
    private String staffName;     // staff_name
    private String phone;         // phone
    private String skill;         // skill
    private String status;        // 在职/离职
    private BigDecimal avgScore;  // avg_score
}
