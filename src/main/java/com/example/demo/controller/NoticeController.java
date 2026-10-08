package com.example.demo.controller;

import com.example.demo.bean.Notice;
import com.example.demo.mapper.NoticeMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notice")
@CrossOrigin
public class NoticeController {

    @Resource
    private NoticeMapper noticeMapper;

    //所有人都可以查询公告
    @GetMapping("/list")
    public List<Notice> list(){
        return noticeMapper.findAll();
    }

    //新增公告，仅管理员调用（前端做按钮隐藏）
    @PostMapping("/add")
    public String add(@RequestBody Notice notice){
        noticeMapper.add(notice);
        return "ok";
    }

    //修改公告，仅管理员调用
    @PutMapping("/update")
    public String update(@RequestBody Notice notice){
        noticeMapper.update(notice);
        return "ok";
    }

    //删除公告，仅管理员调用
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        noticeMapper.delete(id);
        return "ok";
    }

    @GetMapping("/{id}")
    public Notice getById(@PathVariable Integer id){
        return noticeMapper.findById(id);
    }
}
