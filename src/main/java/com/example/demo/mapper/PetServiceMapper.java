package com.example.demo.mapper;

import com.example.demo.bean.PetService;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface PetServiceMapper {

    //所有人都可以查询全部服务
    @Select("select * from pet_service")
    List<PetService> findAll();

    //新增服务 管理员
    @Insert("insert into pet_service(service_name,price,description) values(#{serviceName},#{price},#{description})")
    int add(PetService petService);

    //修改服务 管理员
    @Update("update pet_service set service_name=#{serviceName},price=#{price},description=#{description} where id=#{id}")
    int update(PetService petService);

    //删除服务 管理员
    @Delete("delete from pet_service where id=#{id}")
    int delete(Integer id);

    @Select("select * from pet_service where id=#{id}")
    PetService findById(Integer id);
}
