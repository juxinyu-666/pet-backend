package com.example.demo.bean;

import java.math.BigDecimal;

public class Reserve {
    private Integer id;
    private String petName;
    private String serviceName;
    private String username;
    private Integer petId;        // pet_id 预约的宠物ID（归属校验）
    private String reserveDate;   // reserve_date 预约日期 yyyy-MM-dd
    private String reserveTime;   // reserve_time 预约时段 如 09:00-10:00
    private String startTime;     // start_time 实际开始服务时间（管理员点击开始服务）
    private String finishTime;    // finish_time 实际完成服务时间（管理员点击完成服务）
    private Integer serviceMinutes;// 实际服务分钟数（SQL计算，非表字段）
    private Integer staffId;      // staff_id 选择店员
    private String staffName;     // staff_name 店员姓名（联表查询，非表字段）
    private BigDecimal price;     // price 预约时服务价格快照
    private Integer conflictCount;// 同一店员同日期同时段的其他预约数（联表计算，非表字段）
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getConflictCount() {
        return conflictCount;
    }

    public void setConflictCount(Integer conflictCount) {
        this.conflictCount = conflictCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getServiceMinutes() {
        return serviceMinutes;
    }

    public void setServiceMinutes(Integer serviceMinutes) {
        this.serviceMinutes = serviceMinutes;
    }
}
