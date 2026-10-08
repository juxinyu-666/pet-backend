package com.example.demo.bean;

import lombok.Data;

@Data
public class Pet {
    private Integer petId;        // pet_id
    private String petName;       // pet_name
    private String variety;      // variety 品种（刚才错写成type）
    private Integer age;          // age
    private String gender;        // gender 性别
    private String health;        // health 健康
    private Integer ownerId;      // owner_id
}
