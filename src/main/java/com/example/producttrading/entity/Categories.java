package com.example.producttrading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author guoxin
 * @date 2026年05月05日 0:04
 */
@Data
@TableName("r_categories")
public class Categories {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long deleted;
    private Date createTime;
    private Date updateTime;

}
