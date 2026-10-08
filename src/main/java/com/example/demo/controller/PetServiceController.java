package com.example.demo.controller;

import com.example.demo.bean.PetService;
import com.example.demo.mapper.PetServiceMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/petService")
@CrossOrigin
public class PetServiceController {

    @Resource
    private PetServiceMapper petServiceMapper;

    //所有人查看服务列表
    @GetMapping("/list")
    public List<PetService> list(){
        return petServiceMapper.findAll();
    }

    //新增服务，管理员调用
    @PostMapping("/add")
    public String add(@RequestBody PetService petService){
        petServiceMapper.add(petService);
        return "ok";
    }

    //修改服务，管理员调用
    @PutMapping("/update")
    public String update(@RequestBody PetService petService){
        petServiceMapper.update(petService);
        return "ok";
    }

    //删除服务，管理员调用
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        petServiceMapper.delete(id);
        return "ok";
    }

    @GetMapping("/{id}")
    public PetService getById(@PathVariable Integer id){
        return petServiceMapper.findById(id);
    }
}
