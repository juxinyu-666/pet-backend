package com.example.demo.controller;
import com.example.demo.bean.Owner;
import com.example.demo.mapper.OwnerMapper;
import com.example.demo.mapper.PetMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/owner")
@CrossOrigin
public class OwnerController {
    @Resource
    private OwnerMapper ownerMapper;
    @Resource
    private PetMapper petMapper;
    @GetMapping("/list")
    public List<Owner> list(){ return ownerMapper.findAll(); }
    @GetMapping("/byUser/{userId}")
    public Owner byUser(@PathVariable Integer userId){ return ownerMapper.findByUserId(userId); }
    @PostMapping("/add")
    public String add(@RequestBody Owner o){ ownerMapper.add(o); return "ok"; }

    @PutMapping("/update")
    public String update(@RequestBody Owner o){ ownerMapper.update(o); return "ok"; }

    /**
     * 删除主人：先删除其名下的宠物（级联），再删除主人，避免外键报错
     */
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        petMapper.delByOwnerId(id);
        ownerMapper.delete(id);
        return "ok";
    }
}
