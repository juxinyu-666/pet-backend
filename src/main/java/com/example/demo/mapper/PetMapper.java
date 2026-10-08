package com.example.demo.mapper;
import com.example.demo.bean.Pet;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface PetMapper {
    @Select("SELECT * FROM pet")
    List<Pet> findAll();

    @Select("SELECT * FROM pet WHERE owner_id=#{ownerId}")
    List<Pet> findByOwnerId(Integer ownerId);

    @Select("SELECT * FROM pet WHERE pet_id=#{petId}")
    Pet findById(Integer petId);

    // 校验宠物是否属于指定用户（pet -> owner -> user 链路）
    @Select("SELECT COUNT(*) FROM pet p JOIN owner o ON p.owner_id=o.owner_id " +
            "JOIN user u ON o.user_id=u.user_id WHERE p.pet_id=#{petId} AND u.username=#{username}")
    int belongsToUser(Integer petId, String username);

    @Insert("INSERT INTO pet(pet_name,variety,age,gender,health,owner_id) VALUES(#{petName},#{variety},#{age},#{gender},#{health},#{ownerId})")
    int add(Pet pet);

    @Delete("DELETE FROM pet WHERE pet_id=#{petId}")
    int delById(Integer petId);

    // 删除某主人的全部宠物（删除主人时级联清理）
    @Delete("DELETE FROM pet WHERE owner_id=#{ownerId}")
    int delByOwnerId(Integer ownerId);

    @Update("UPDATE pet SET pet_name=#{petName},variety=#{variety},age=#{age},gender=#{gender},health=#{health},owner_id=#{ownerId} WHERE pet_id=#{petId}")
    int updateById(Pet pet);
}
