package com.example.demo.mapper;

import com.example.demo.bean.Rating;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface RatingMapper {

    @Select("select t.*, s.staff_name as staffName from rating t left join staff s on t.staff_id=s.staff_id order by t.rating_id desc")
    List<Rating> findAll();

    @Select("select t.*, s.staff_name as staffName from rating t left join staff s on t.staff_id=s.staff_id " +
            "where t.staff_id=#{staffId} order by t.rating_id desc")
    List<Rating> findByStaffId(Integer staffId);

    @Select("select count(*) from rating where reserve_id=#{reserveId}")
    int countByReserveId(Integer reserveId);

    @Insert("insert into rating(reserve_id,staff_id,username,score,comment) values(#{reserveId},#{staffId},#{username},#{score},#{comment})")
    int add(Rating rating);

    // 评分后重算店员平均分
    @Update("update staff set avg_score=(select round(avg(score),1) from rating where staff_id=#{staffId}) where staff_id=#{staffId}")
    int refreshAvgScore(Integer staffId);
}
