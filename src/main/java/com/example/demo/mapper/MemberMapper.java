package com.example.demo.mapper;

import com.example.demo.bean.Member;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MemberMapper {

    // 全部会员（带用户名）
    @Select("select m.*, u.username from member m left join user u on m.user_id=u.user_id")
    List<Member> findAll();

    // 按用户账号查会员
    @Select("select m.*, u.username from member m left join user u on m.user_id=u.user_id where u.username=#{username}")
    Member findByUsername(String username);

    // 按用户ID查会员（同一用户可能被开通多次，取最新一条）
    @Select("select m.*, u.username from member m left join user u on m.user_id=u.user_id " +
            "where m.user_id=#{userId} order by m.member_id desc limit 1")
    Member findByUserId(Integer userId);

    @Insert("insert into member(user_id,level,points) values(#{userId},#{level},#{points})")
    int add(Member member);

    @Update("update member set level=#{level},points=#{points} where member_id=#{memberId}")
    int update(Member member);

    @Delete("delete from member where member_id=#{memberId}")
    int delete(Integer memberId);
}
