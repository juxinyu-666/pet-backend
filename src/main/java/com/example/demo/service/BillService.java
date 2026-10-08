package com.example.demo.service;

import com.example.demo.bean.Bill;

/**
 * 账单业务接口
 */
public interface BillService {

    /**
     * 支付账单（事务）：更新账单为已支付 + 同步会员积分（扣已用积分、返消费积分）
     */
    String payBill(Bill bill);
}
