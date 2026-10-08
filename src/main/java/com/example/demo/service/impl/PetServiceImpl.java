package com.example.demo.service.impl;

import com.example.demo.bean.Pet;
import com.example.demo.mapper.PetMapper;
import com.example.demo.service.PetService;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.List;

@Service
public class PetServiceImpl implements PetService {
    @Resource
    private PetMapper petMapper;

    @Override
    public List<Pet> findAll() {
        return petMapper.findAll();
    }

    @Override
    public List<Pet> findByOwnerId(Integer ownerId) {
        return petMapper.findByOwnerId(ownerId);
    }

    @Override
    public int add(Pet pet) {
        return petMapper.add(pet);
    }

    @Override
    public int delById(Integer petId) {
        return petMapper.delById(petId);
    }

    @Override
    public int updateById(Pet pet) {
        return petMapper.updateById(pet);
    }
}
