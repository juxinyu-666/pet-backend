package com.example.demo.service;
import com.example.demo.bean.Pet;
import java.util.List;

public interface PetService {
    List<Pet> findAll();
    List<Pet> findByOwnerId(Integer ownerId);
    int add(Pet pet);
    int delById(Integer petId);
    int updateById(Pet pet);
}
