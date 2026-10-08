package com.example.demo.mapper;

import com.example.demo.bean.Staff;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface StaffMapper {

    @Select("select * from staff")
    List<Staff> findAll();

    @Select("select * from staff where staff_id=#{staffId}")
    Staff findById(Integer staffId);

    @Insert("insert into staff(staff_name,phone,skill,status,avg_score) values(#{staffName},#{phone},#{skill},#{status},#{avgScore})")
    int add(Staff staff);

    @Update("update staff set staff_name=#{staffName},phone=#{phone},skill=#{skill},status=#{status},avg_score=#{avgScore} where staff_id=#{staffId}")
    int update(Staff staff);

    @Delete("delete from staff where staff_id=#{staffId}")
    int delete(Integer staffId);

    // 某日期某时段空闲的店员：排除该时段已有【已同意/进行中】预约的店员
    @Select("select s.* from staff s where s.status='在职' and s.staff_id not in (" +
            "select r.staff_id from reserve r where r.reserve_date=#{date} and r.reserve_time=#{time} " +
            "and r.status in ('已同意','进行中'))")
    List<Staff> findAvailable(String date, String time);

    // 某日期每个店员的被占用时段列表（用于展示“几点后有空”）
    @Select("select r.staff_id as staffId, group_concat(r.reserve_time order by r.reserve_time separator '、') as busyTimes " +
            "from reserve r where r.reserve_date=#{date} and r.status in ('已同意','进行中') group by r.staff_id")
    List<Map<String, Object>> findBusyByDate(String date);

    // 店员业绩：完成单数、完成项目营业额、平均评分
    @Select("select s.staff_id as staffId, s.staff_name as staffName, s.avg_score as avgScore, " +
            "count(r.id) as doneCount, ifnull(sum(r.price),0) as doneAmount " +
            "from staff s left join reserve r on r.staff_id=s.staff_id and r.status='已完成' " +
            "group by s.staff_id order by s.staff_id")
    List<Map<String, Object>> performance();
}
