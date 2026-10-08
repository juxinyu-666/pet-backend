package com.example.demo.controller;

import com.example.demo.bean.Bill;
import com.example.demo.mapper.BillMapper;
import com.example.demo.service.BillService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/bill")
@CrossOrigin
public class BillController {

    @Resource
    private BillMapper billMapper;
    @Resource
    private BillService billService;

    // 全部账单（管理员）
    @GetMapping("/list")
    public List<Bill> list() {
        return billMapper.findAll();
    }

    // 我的账单（主人）
    @GetMapping("/my")
    public List<Bill> my(@RequestParam String username) {
        return billMapper.findByUsername(username);
    }

    /**
     * 支付账单（线上=模拟支付，线下=到店支付）
     * 规则：50积分=1元抵扣；实付金额 1元=1积分返给会员
     * 事务：账单更新 + 积分结算两步写库，由 BillService 统一管理（@Transactional）
     */
    @PostMapping("/pay")
    public String pay(@RequestBody Bill bill) {
        return billService.payBill(bill);
    }

    /**
     * 管理员修改未支付账单金额（线下顾客临时增加项目等）
     */
    @PostMapping("/updateAmount")
    public String updateAmount(@RequestBody Bill bill) {
        if (bill.getBillId() == null || bill.getTotalAmount() == null) {
            return "no amount";
        }
        Bill b = billMapper.findById(bill.getBillId());
        if (b == null) {
            return "not found";
        }
        if ("已支付".equals(b.getPayStatus())) {
            return "paid";
        }
        if (bill.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
            return "negative";
        }
        billMapper.updateAmount(bill);
        return "ok";
    }
}
