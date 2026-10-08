package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface DashboardMapper {

    @Select("select count(*) from reserve where reserve_date=CURDATE()")
    int todayReserve();

    @Select("select count(*) from reserve where status='待审核'")
    int pendingReserve();

    @Select("select count(*) from reserve where status='进行中'")
    int doingReserve();

    @Select("select count(*) from reserve where status='已完成'")
    int doneReserve();

    @Select("select IFNULL(sum(pay_amount),0) from bill where pay_status='已支付'")
    BigDecimal totalIncome();

    @Select("select count(*) from user")
    int userCount();

    @Select("select count(*) from pet")
    int petCount();

    @Select("select count(*) from staff")
    int staffCount();

    @Select("select count(*) from member")
    int memberCount();

    @Select("select count(*) from pet_service")
    int serviceCount();

    @Select("select count(*) from message")
    int messageCount();

    @Select("select count(*) from notice")
    int noticeCount();

    // 近7天每日新增预约数（按预约日期）
    @Select("select DATE_FORMAT(reserve_date,'%Y-%m-%d') as d, count(*) as cnt from reserve " +
            "where reserve_date >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) group by reserve_date")
    List<Map<String, Object>> reserveTrend();

    // 近7天每日营业额（按账单创建日期，已支付）
    @Select("select DATE_FORMAT(create_time,'%Y-%m-%d') as d, IFNULL(SUM(pay_amount),0) as amt from bill " +
            "where pay_status='已支付' and create_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) group by DATE_FORMAT(create_time,'%Y-%m-%d')")
    List<Map<String, Object>> incomeTrend();

    // 预约状态分布
    @Select("select status as name, count(*) as value from reserve group by status")
    List<Map<String, Object>> statusDist();

    // 服务项目订单占比
    @Select("select service_name as name, count(*) as value from reserve group by service_name")
    List<Map<String, Object>> serviceDist();
}
