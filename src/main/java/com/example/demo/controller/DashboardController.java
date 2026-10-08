package com.example.demo.controller;

import com.example.demo.mapper.DashboardMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashboardController {

    @Resource
    private DashboardMapper dashboardMapper;

    /**
     * 管理员总览：店里所有信息
     */
    @GetMapping("/summary")
    public Map<String, Object> summary() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("todayReserve", dashboardMapper.todayReserve());
        map.put("pendingReserve", dashboardMapper.pendingReserve());
        map.put("doingReserve", dashboardMapper.doingReserve());
        map.put("doneReserve", dashboardMapper.doneReserve());
        map.put("totalIncome", dashboardMapper.totalIncome());
        map.put("userCount", dashboardMapper.userCount());
        map.put("petCount", dashboardMapper.petCount());
        map.put("staffCount", dashboardMapper.staffCount());
        map.put("memberCount", dashboardMapper.memberCount());
        map.put("serviceCount", dashboardMapper.serviceCount());
        map.put("messageCount", dashboardMapper.messageCount());
        map.put("noticeCount", dashboardMapper.noticeCount());
        return map;
    }

    /**
     * 图表数据：近7天预约/营业额趋势、状态分布、服务占比
     */
    @GetMapping("/chart")
    public Map<String, Object> chart() {
        Map<String, Object> map = new LinkedHashMap<>();

        // 近7天日期（今天往前数7天），缺失补0
        LocalDate today = LocalDate.now();
        List<String> days = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            days.add(today.minusDays(i).toString());
        }
        map.put("days", days);

        Map<String, Integer> reserveMap = new HashMap<>();
        for (Map<String, Object> row : dashboardMapper.reserveTrend()) {
            reserveMap.put(String.valueOf(row.get("d")), ((Number) row.get("cnt")).intValue());
        }
        List<Integer> reserveTrend = new ArrayList<>();
        for (String d : days) {
            reserveTrend.add(reserveMap.getOrDefault(d, 0));
        }
        map.put("reserveTrend", reserveTrend);

        Map<String, BigDecimal> incomeMap = new HashMap<>();
        for (Map<String, Object> row : dashboardMapper.incomeTrend()) {
            incomeMap.put(String.valueOf(row.get("d")), (BigDecimal) row.get("amt"));
        }
        List<BigDecimal> incomeTrend = new ArrayList<>();
        for (String d : days) {
            incomeTrend.add(incomeMap.getOrDefault(d, BigDecimal.ZERO));
        }
        map.put("incomeTrend", incomeTrend);

        map.put("statusDist", dashboardMapper.statusDist());
        map.put("serviceDist", dashboardMapper.serviceDist());
        return map;
    }
}
