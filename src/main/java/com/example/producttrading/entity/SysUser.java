package com.example.producttrading.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author guoxin
 * @date 2026年04月03日 14:30
 */
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity{
    @TableId(value = "user_id", type = IdType.AUTO)
    @Alias("用户编号")
    private Integer userId;
    @Alias("用户名")
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String sex;
    private String phone;
    private String email;
    private Long status ;
    private Long deleted;
    private Date createTime;
    private Date updateTime;
    @TableField("type")
    private String type;
}
