package com.example.demo.controller;

import com.example.demo.bean.Message;
import com.example.demo.mapper.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/message")
@CrossOrigin
public class MessageController {

    @Resource
    private MessageMapper messageMapper;

    //管理员：全部留言
    @GetMapping("/all")
    public List<Message> all(){
        return messageMapper.findAll();
    }

    //普通用户：我的留言
    @GetMapping("/my")
    public List<Message> my(@RequestParam String username){
        return messageMapper.findMyMessage(username);
    }

    //新增留言（普通用户）
    @PostMapping("/add")
    public String add(@RequestBody Message message){
        messageMapper.add(message);
        return "ok";
    }

    //管理员回复留言
    @PostMapping("/reply")
    public String reply(@RequestBody Message message){
        messageMapper.reply(message);
        return "ok";
    }

    //删除留言（管理员）
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        messageMapper.delete(id);
        return "ok";
    }
}
