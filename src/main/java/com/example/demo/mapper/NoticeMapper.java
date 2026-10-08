package com.example.demo.mapper;

import com.example.demo.bean.Notice;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface NoticeMapper {

    //查询全部公告，所有人都可以看
    @Select("select * from notice order by create_time desc")
    List<Notice> findAll();

    //新增公告【管理员操作】
    @Insert("insert into notice(title,content) values(#{title},#{content})")
    int add(Notice notice);

    //修改公告【管理员操作】
    @Update("update notice set title=#{title},content=#{content} where id=#{id}")
    int update(Notice notice);

    //删除公告【管理员操作】
    @Delete("delete from notice where id=#{id}")
    int delete(Integer id);

    //根据id查询单条公告
    @Select("select * from notice where id=#{id}")
    Notice findById(Integer id);
}
