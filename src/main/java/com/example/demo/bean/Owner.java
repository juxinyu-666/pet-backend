package com.example.demo.bean;
import lombok.Data;
@Data
public class Owner {
    private Integer ownerId;
    private String realName;   //对应数据库 real_name
    private String phone;
    private String address;
    private Integer userId;    //对应数据库 user_id，关联用户账号
    private String username;   //关联用户名（join user 查出，非表字段）
}
