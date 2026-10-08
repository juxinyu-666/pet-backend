package com.example.demo.mapper;

import com.example.demo.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user")
    List<User> findAll();

    @Select("select * from user where username=#{username} and password=#{password}")
    List<User> selectUser(String username, String password);

    @Select("select * from user where username = #{username}")
    User selectByUsername(String username);

    //传入User对象，设置默认role=宠物主人，phone为空字符串
    @Insert("insert into user(username,password,role,phone) values(#{username},#{password},'宠物主人',NULL)")
    int insertUser(User user);
}
