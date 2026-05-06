package com.example.producttrading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author guoxin
 * @date 2026年04月08日 16:49
 */
@TableName("r_products")
@Data
public class Products extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private long id;
    private String name;
    private String price;
    private String description;
    private String category;
    private String imageUrl;
    private String producer;
    private String unit;
    private long sellCount;
    private String content;
    private long stock;
    private String status;
    private long userId;
    @TableField(exist = false)
    private SysUser sysUser;
    private String createUser;
    private Date createTime;
    private Date updateTime;
    private long deleted = 0;

}
