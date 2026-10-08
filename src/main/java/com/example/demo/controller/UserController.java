package com.example.demo.controller;

import com.example.demo.bean.User;
import com.example.demo.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource
    private UserMapper userMapper;

    //登录接口，返回完整用户（包含role角色）
    @PostMapping("/login")
    public User login(@RequestBody User user){
        List<User> list = userMapper.selectUser(user.getUsername(),user.getPassword());
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    //查询全部用户【管理员专用】
    @GetMapping("/list")
    public List<User> findAll(){
        return userMapper.findAll();
    }

    //注册接口：注册的用户固定角色=宠物主人
    @PostMapping("/register")
    public String register(@RequestBody User user){
        User existUser = userMapper.selectByUsername(user.getUsername());
        if(existUser != null){
            return "exist";
        }
        userMapper.insertUser(user);
        return "ok";
    }
}
