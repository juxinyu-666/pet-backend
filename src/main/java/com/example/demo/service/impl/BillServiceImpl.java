package com.example.demo.service.impl;

import com.example.demo.bean.Bill;
import com.example.demo.bean.Member;
import com.example.demo.bean.Reserve;
import com.example.demo.bean.User;
import com.example.demo.mapper.BillMapper;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.mapper.ReserveMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.BillService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 账单业务实现
 * 支付流程涉及「账单状态更新 + 会员积分结算」两步写库操作，
 * 使用 @Transactional 声明式事务保证两步要么一起成功、要么一起回滚。
 */
@Service
public class BillServiceImpl implements BillService {

    @Resource
    private BillMapper billMapper;
    @Resource
    private ReserveMapper reserveMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MemberMapper memberMapper;

    /**
     * 支付账单（事务）：
     * 1) 校验账单存在且未支付
     * 2) 按积分规则计算实付金额（50积分=1元）
     * 3) 更新账单：已支付 + 积分使用 + 实付金额
     * 4) 同步会员积分：扣已用积分 + 按实付金额返积分（1元=1分）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String payBill(Bill bill) {
        Bill b = billMapper.findById(bill.getBillId());
        if (b == null) {
            return "not found";
        }
        if ("已支付".equals(b.getPayStatus())) {
            return "paid";
        }
        BigDecimal total = b.getTotalAmount() == null ? BigDecimal.ZERO : b.getTotalAmount();
        int pointsUsed = bill.getPointsUsed() == null ? 0 : bill.getPointsUsed();
        // 积分规则：50积分=1元；可抵金额=积分/50（向下取整到元），且不能超过总金额
        int maxPoints = total.multiply(BigDecimal.valueOf(50)).intValue();
        if (pointsUsed > maxPoints) {
            pointsUsed = maxPoints;
        }
        BigDecimal usedMoney = BigDecimal.valueOf(pointsUsed / 50); // 50积分抵1元
        BigDecimal payAmount = total.subtract(usedMoney);

        Bill update = new Bill();
        update.setBillId(b.getBillId());
        update.setPointsUsed(pointsUsed);
        update.setPayAmount(payAmount);
        update.setPayMethod(bill.getPayMethod() == null ? "线下" : bill.getPayMethod());
        billMapper.pay(update);          // 第 1 步写库：账单

        // 会员积分结算：扣已用积分 + 返消费积分(1元=1分)
        Reserve reserve = reserveMapper.findById(b.getReserveId());
        if (reserve != null && reserve.getUsername() != null) {
            User user = userMapper.selectByUsername(reserve.getUsername());
            if (user != null) {
                Member member = memberMapper.findByUserId(user.getUserId());
                if (member != null) {
                    int newPoints = member.getPoints() - pointsUsed + payAmount.intValue();
                    if (newPoints < 0) {
                        newPoints = 0;
                    }
                    member.setPoints(newPoints);
                    memberMapper.update(member);   // 第 2 步写库：会员积分
                }
            }
        }
        return "ok";
    }
}
