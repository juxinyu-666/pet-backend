package com.example.demo.controller;

import com.example.demo.bean.Rating;
import com.example.demo.mapper.RatingMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rating")
@CrossOrigin
public class RatingController {

    @Resource
    private RatingMapper ratingMapper;

    // 全部评分（管理员）
    @GetMapping("/list")
    public List<Rating> list() {
        return ratingMapper.findAll();
    }

    // 某店员的评分列表
    @GetMapping("/staff/{staffId}")
    public List<Rating> byStaff(@PathVariable Integer staffId) {
        return ratingMapper.findByStaffId(staffId);
    }

    // 是否已对该预约评过分（前端控制按钮）
    @GetMapping("/exists")
    public int exists(@RequestParam Integer reserveId) {
        return ratingMapper.countByReserveId(reserveId);
    }

    // 提交评分（服务完成后）
    @PostMapping("/add")
    public String add(@RequestBody Rating rating) {
        ratingMapper.add(rating);
        // 同步重算店员平均分
        ratingMapper.refreshAvgScore(rating.getStaffId());
        return "ok";
    }
}
