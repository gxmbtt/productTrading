package com.example.producttrading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author guoxin
 * @date 2026年04月12日 1:10
 */
@Data
@TableName("r_cart")
public class Cart extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private long id;
    private String name;
    private String type;
    private String price;
    private String unit;
    private long userId;
    private Date createTime = new Date();
    private String createUser;
    private long status;
    private String updateTime;
    private String updateUser;
    private long goodsId;
    private java.util.Date payTime;
    private long count;
    private String image;

}

