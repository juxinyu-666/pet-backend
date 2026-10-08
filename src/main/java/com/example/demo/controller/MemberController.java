package com.example.demo.controller;

import com.example.demo.bean.Member;
import com.example.demo.mapper.MemberMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/member")
@CrossOrigin
public class MemberController {

    @Resource
    private MemberMapper memberMapper;

    // 全部会员（管理员）
    @GetMapping("/list")
    public List<Member> list() {
        return memberMapper.findAll();
    }

    // 我的会员信息（主人）
    @GetMapping("/my")
    public Member my(@RequestParam String username) {
        return memberMapper.findByUsername(username);
    }

    // 按用户ID查（前端有时只有 userId）
    @GetMapping("/byUser/{userId}")
    public Member byUser(@PathVariable Integer userId) {
        return memberMapper.findByUserId(userId);
    }

    // 管理员：开通会员
    @PostMapping("/add")
    public String add(@RequestBody Member member) {
        memberMapper.add(member);
        return "ok";
    }

    // 管理员：修改会员等级/积分
    @PutMapping("/update")
    public String update(@RequestBody Member member) {
        memberMapper.update(member);
        return "ok";
    }

    // 管理员：取消会员
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        memberMapper.delete(id);
        return "ok";
    }
}
