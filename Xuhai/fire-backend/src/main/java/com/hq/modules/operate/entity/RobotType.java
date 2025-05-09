package com.hq.modules.operate.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

@Data
@TableName("robot_types")
public class RobotType {
    @TableId(value = "type_id", type = IdType.AUTO)
    private Integer typeId;

    private String typeName;
    private String description;
    private Date createdAt;
}