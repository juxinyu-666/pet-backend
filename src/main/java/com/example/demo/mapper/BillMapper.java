package com.example.demo.mapper;

import com.example.demo.bean.Bill;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BillMapper {

    // 全部账单（管理员，带实际服务时长）
    @Select("select b.*, r.pet_name as petName, r.service_name as serviceName, r.username, " +
            "(case when r.status='已完成' and r.start_time is not null and r.finish_time is not null " +
            "then timestampdiff(MINUTE, r.start_time, r.finish_time) else null end) as serviceMinutes " +
            "from bill b left join reserve r on b.reserve_id=r.id order by b.bill_id desc")
    List<Bill> findAll();

    // 某用户的全部账单（通过预约关联，带实际服务时长）
    @Select("select b.*, r.pet_name as petName, r.service_name as serviceName, r.username, " +
            "(case when r.status='已完成' and r.start_time is not null and r.finish_time is not null " +
            "then timestampdiff(MINUTE, r.start_time, r.finish_time) else null end) as serviceMinutes " +
            "from bill b left join reserve r on b.reserve_id=r.id " +
            "where r.username=#{username} order by b.bill_id desc")
    List<Bill> findByUsername(String username);

    // 按预约查账单
    @Select("select * from bill where reserve_id=#{reserveId}")
    Bill findByReserveId(Integer reserveId);

    @Select("select * from bill where bill_id=#{billId}")
    Bill findById(Integer billId);

    // 服务完成时生成账单（未支付）
    @Insert("insert into bill(reserve_id,total_amount,points_used,pay_amount,pay_status) " +
            "values(#{reserveId},#{totalAmount},0,#{totalAmount},'未支付')")
    int add(Bill bill);

    // 支付：更新积分使用、实付金额、支付方式、支付状态
    @Update("update bill set points_used=#{pointsUsed},pay_amount=#{payAmount},pay_method=#{payMethod},pay_status='已支付' where bill_id=#{billId}")
    int pay(Bill bill);

    // 管理员修改未支付账单金额（线下临时加项目）
    @Update("update bill set total_amount=#{totalAmount}, pay_amount=#{totalAmount} where bill_id=#{billId} and pay_status='未支付'")
    int updateAmount(Bill bill);
}
