package com.example.producttrading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author guoxin
 * @date 2026年05月06日 15:59
 */
@Data
@TableName("r_banner")
public class Banner extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private long id;
    private String bannerImg;
    private String createUser;
    private Date createTime;

}

