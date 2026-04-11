package com.example.producttrading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author guoxin
 * @date 2026年04月12日 0:11
 */
@Data
@TableName("r_product_comment")
public class ProductComment extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private long id;
    private long userId;
    private long pid;
    private String name;
    private String comment;
    private String createUser;
    private String createTime;

}