package com.example.demo.mapper;

import com.example.demo.bean.Message;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MessageMapper {

    //管理员：查询全部留言
    @Select("select * from message order by create_time desc")
    List<Message> findAll();

    //普通用户：只查自己的留言
    @Select("select * from message where username = #{username} order by create_time desc")
    List<Message> findMyMessage(String username);

    //新增留言（普通用户）
    @Insert("insert into message(username,content) values(#{username},#{content})")
    int add(Message message);

    //管理员回复留言
    @Update("update message set reply=#{reply}, reply_time=now() where id=#{id}")
    int reply(Message message);

    //删除留言（管理员）
    @Delete("delete from message where id=#{id}")
    int delete(Integer id);
}
