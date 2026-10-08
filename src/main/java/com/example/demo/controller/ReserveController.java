package com.example.demo.controller;

import com.example.demo.bean.Reserve;
import com.example.demo.mapper.PetMapper;
import com.example.demo.mapper.ReserveMapper;
import com.example.demo.service.ReserveService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reserve")
@CrossOrigin
public class ReserveController {

    @Resource
    private ReserveMapper reserveMapper;
    @Resource
    private PetMapper petMapper;
    @Resource
    private ReserveService reserveService;

    /**
     * 管理员接口：获取全部预约
     */
    @GetMapping("/all")
    public List<Reserve> getAll(){
        return reserveMapper.findAll();
    }

    /**
     * 宠物主人接口：获取我自己的预约
     */
    @GetMapping("/my")
    public List<Reserve> getMy(@RequestParam String username){
        return reserveMapper.findMyReserve(username);
    }

    /**
     * 提交预约：校验宠物确实属于当前预约用户（防止用别人的宠物预约）
     */
    @PostMapping("/add")
    public String add(@RequestBody Reserve reserve){
        if(reserve.getPetId() == null){
            return "no pet";
        }
        // 宠物必须属于该用户：pet -> owner -> user 链路校验
        if(petMapper.belongsToUser(reserve.getPetId(), reserve.getUsername()) <= 0){
            return "not your pet";
        }
        reserveMapper.addReserve(reserve);
        return "ok";
    }

    /**
     * 管理员：同意预约（校验该店员该时段是否已被占用）
     */
    @PutMapping("/accept/{id}")
    public String accept(@PathVariable Integer id){
        Reserve r = reserveMapper.findById(id);
        if(r != null && r.getStaffId() != null && r.getReserveDate() != null && r.getReserveTime() != null){
            if(reserveMapper.countConflict(r.getStaffId(), r.getReserveDate(), r.getReserveTime(), id) > 0){
                return "busy";
            }
        }
        reserveMapper.accept(id);
        return "ok";
    }

    /**
     * 管理员：拒绝预约
     */
    @PutMapping("/reject/{id}")
    public String reject(@PathVariable Integer id){
        reserveMapper.reject(id);
        return "ok";
    }

    /**
     * 管理员：开始服务（已同意 -> 进行中，校验时段冲突）
     */
    @PutMapping("/start/{id}")
    public String start(@PathVariable Integer id){
        Reserve r = reserveMapper.findById(id);
        if(r != null && r.getStaffId() != null && r.getReserveDate() != null && r.getReserveTime() != null){
            if(reserveMapper.countConflict(r.getStaffId(), r.getReserveDate(), r.getReserveTime(), id) > 0){
                return "busy";
            }
        }
        reserveMapper.start(id);
        return "ok";
    }

    /**
     * 管理员：完成服务（进行中 -> 已完成）并自动生成账单
     * 事务：状态更新 + 账单生成两步写库，由 ReserveService 统一管理（@Transactional）
     */
    @PutMapping("/finish/{id}")
    public String finish(@PathVariable Integer id){
        return reserveService.finishReserve(id);
    }

    /**
     * 主人：取消未生效预约（待审核/已同意）
     */
    @PutMapping("/cancel/{id}")
    public String cancel(@PathVariable Integer id){
        reserveMapper.cancel(id);
        return "ok";
    }

    /**
     * 删除预约
     */
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        reserveMapper.delete(id);
        return "ok";
    }
}
