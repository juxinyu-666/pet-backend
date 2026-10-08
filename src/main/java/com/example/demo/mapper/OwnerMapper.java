package com.example.demo.mapper;
import com.example.demo.bean.Owner;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface OwnerMapper {
    @Select("select o.*, u.username from owner o left join user u on o.user_id=u.user_id order by o.owner_id")
    List<Owner> findAll();

    @Select("select o.*, u.username from owner o left join user u on o.user_id=u.user_id where o.user_id=#{userId}")
    Owner findByUserId(Integer userId);

    @Insert("insert into owner(real_name,phone,address,user_id) values(#{realName},#{phone},#{address},#{userId})")
    int add(Owner owner);

    @Update("update owner set real_name=#{realName},phone=#{phone},address=#{address},user_id=#{userId} where owner_id=#{ownerId}")
    int update(Owner owner);

    @Delete("delete from owner where owner_id=#{ownerId}")
    int delete(Integer ownerId);
}
