package com.hq.modules.operate.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;
@Data
@TableName("xunchaUsers")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;
    private String email;

    @TableField("first_name")
    private String firstName;

    @TableField("last_name")
    private String lastName;

    private String phone;
    private String avatar;
    private Integer status;

    @TableField("last_login")
    private Date lastLogin;

    @TableField("created_at")
    private Date createdAt;

    @TableField("updated_at")
    private Date updatedAt;

}