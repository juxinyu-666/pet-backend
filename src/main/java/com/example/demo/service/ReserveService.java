package com.example.demo.service;

/**
 * 预约业务接口
 */
public interface ReserveService {

    /**
     * 完成服务（事务）：更新预约状态为已完成 + 自动生成未支付账单
     */
    String finishReserve(Integer id);
}
