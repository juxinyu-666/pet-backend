package com.example.demo.service.impl;

import com.example.demo.bean.Bill;
import com.example.demo.bean.Reserve;
import com.example.demo.mapper.BillMapper;
import com.example.demo.mapper.ReserveMapper;
import com.example.demo.service.ReserveService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 预约业务实现
 * 完成服务涉及「更新预约状态 + 生成账单」两步写库操作，
 * 使用 @Transactional 声明式事务保证两步一致（失败自动回滚，不产生孤儿账单）。
 */
@Service
public class ReserveServiceImpl implements ReserveService {

    @Resource
    private ReserveMapper reserveMapper;
    @Resource
    private BillMapper billMapper;

    /**
     * 完成服务（事务）：
     * 1) 预约状态：进行中 -> 已完成（写入实际结束时间 finish_time）
     * 2) 自动生成一笔未支付账单（金额取预约时服务价格快照）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String finishReserve(Integer id) {
        reserveMapper.finish(id);   // 第 1 步写库：预约状态
        Reserve reserve = reserveMapper.findById(id);
        if (reserve != null) {
            Bill bill = new Bill();
            bill.setReserveId(id);
            bill.setTotalAmount(reserve.getPrice());
            billMapper.add(bill);   // 第 2 步写库：生成账单
        }
        return "ok";
    }
}
