package com.example.demo.mapper;

import com.example.demo.bean.Reserve;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ReserveMapper {

    // 冲突判断：同一店员、同一日期、同一时段，且状态为 已同意/进行中 的其他预约数
    String CONFLICT_SQL = "(select count(*) from reserve r2 where r2.staff_id=r.staff_id " +
            "and r2.reserve_date=r.reserve_date and r2.reserve_time=r.reserve_time " +
            "and r2.status in ('已同意','进行中') and r2.id<>r.id)";

    // 实际服务分钟数：已完成=完成时间-开始时间；进行中=当前时间-开始时间
    String SERVICE_MINUTES_SQL = "(case when r.status='已完成' and r.start_time is not null and r.finish_time is not null " +
            "then timestampdiff(MINUTE, r.start_time, r.finish_time) " +
            "when r.status='进行中' and r.start_time is not null then timestampdiff(MINUTE, r.start_time, now()) " +
            "else null end)";

    //管理员：查询全部预约（联表带出店员姓名、宠物名与冲突数）
    @Select("select r.*, s.staff_name as staffName, p.pet_name as petName, " + CONFLICT_SQL + " as conflictCount, "
            + SERVICE_MINUTES_SQL + " as serviceMinutes " +
            "from reserve r left join staff s on r.staff_id=s.staff_id left join pet p on r.pet_id=p.pet_id " +
            "order by r.id desc")
    List<Reserve> findAll();

    //普通用户：只查询自己的预约记录
    @Select("select r.*, s.staff_name as staffName, p.pet_name as petName, " + CONFLICT_SQL + " as conflictCount, "
            + SERVICE_MINUTES_SQL + " as serviceMinutes " +
            "from reserve r left join staff s on r.staff_id=s.staff_id left join pet p on r.pet_id=p.pet_id " +
            "where r.username = #{username} order by r.id desc")
    List<Reserve> findMyReserve(String username);

    //按ID查询
    @Select("select r.*, s.staff_name as staffName, p.pet_name as petName, " + CONFLICT_SQL + " as conflictCount, "
            + SERVICE_MINUTES_SQL + " as serviceMinutes " +
            "from reserve r left join staff s on r.staff_id=s.staff_id left join pet p on r.pet_id=p.pet_id " +
            "where r.id=#{id}")
    Reserve findById(Integer id);

    //新增预约（含宠物ID/日期/时段/店员/价格快照）
    @Insert("insert into reserve(pet_id,pet_name,service_name,username,reserve_date,reserve_time,staff_id,price,status) " +
            "values(#{petId},#{petName},#{serviceName},#{username},#{reserveDate},#{reserveTime},#{staffId},#{price},'待审核')")
    int addReserve(Reserve reserve);

    //检查店员时段冲突（排除指定预约ID，用于同意/开始服务前校验）
    @Select("select count(*) from reserve where staff_id=#{staffId} and reserve_date=#{date} " +
            "and reserve_time=#{time} and status in ('已同意','进行中') and id<>#{excludeId}")
    int countConflict(Integer staffId, String date, String time, Integer excludeId);

    //同意预约
    @Update("update reserve set status='已同意' where id=#{id}")
    int accept(Integer id);

    //拒绝预约
    @Update("update reserve set status='已拒绝' where id=#{id}")
    int reject(Integer id);

    //开始服务（管理员操作：已同意 -> 进行中，记录实际开始时间）
    @Update("update reserve set status='进行中', start_time=now() where id=#{id}")
    int start(Integer id);

    //完成服务（管理员操作：进行中 -> 已完成，记录实际完成时间，生成账单）
    @Update("update reserve set status='已完成', finish_time=now() where id=#{id}")
    int finish(Integer id);

    //主人取消未生效预约（待审核/已同意 可取消）
    @Update("update reserve set status='已取消' where id=#{id} and status in ('待审核','已同意')")
    int cancel(Integer id);

    //删除预约
    @Delete("delete from reserve where id=#{id}")
    int delete(Integer id);
}
