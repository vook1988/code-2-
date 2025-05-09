package com.hq.modules.operate.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("robots")
public class Robot {
    @TableId(value = "robot_id", type = IdType.AUTO)
    private Integer robotId;

    private String robotCode;
    private Integer typeId;
    private String name;
    private String model;
    private String manufacturer;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;
    private String status;
    private String currentLocation;
    private Integer batteryLevel;
    private Date lastMaintenance;
    private Date nextMaintenance;
    private Date createdAt;
    private Date updatedAt;

    @TableField(exist = false)
    private RobotType robotType; // 关联的机器人类型信息
}