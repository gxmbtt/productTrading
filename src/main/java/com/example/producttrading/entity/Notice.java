package com.example.producttrading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author guoxin
 * @date 2026年05月07日 22:48
 */
@Data
@TableName("r_notice")
public class Notice extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private long id;
    private String name;
    private String neirong;
    private Date createTime;
    private String createUser;
    private String status;

}
