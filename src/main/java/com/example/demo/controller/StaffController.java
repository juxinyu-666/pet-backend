package com.example.demo.controller;

import com.example.demo.bean.Staff;
import com.example.demo.mapper.StaffMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staff")
@CrossOrigin
public class StaffController {

    @Resource
    private StaffMapper staffMapper;

    // 全部店员（顾客/管理员都可看）
    @GetMapping("/list")
    public List<Staff> list() {
        return staffMapper.findAll();
    }

    // 某日期某时段空闲的店员（顾客选店员用）
    @GetMapping("/available")
    public List<Staff> available(@RequestParam String date, @RequestParam String time) {
        return staffMapper.findAvailable(date, time);
    }

    // 某日期每个店员被占用时段（显示“几点后有空”）
    @GetMapping("/busy")
    public List<Map<String, Object>> busy(@RequestParam String date) {
        return staffMapper.findBusyByDate(date);
    }

    // 店员业绩统计（完成单数/营业额/平均评分）
    @GetMapping("/performance")
    public List<Map<String, Object>> performance() {
        return staffMapper.performance();
    }

    // 管理员：新增店员
    @PostMapping("/add")
    public String add(@RequestBody Staff staff) {
        staffMapper.add(staff);
        return "ok";
    }

    // 管理员：修改店员
    @PutMapping("/update")
    public String update(@RequestBody Staff staff) {
        staffMapper.update(staff);
        return "ok";
    }

    // 管理员：删除店员
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        staffMapper.delete(id);
        return "ok";
    }
}
