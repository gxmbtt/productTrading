package com.example.producttrading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("r_apply")
public class Apply extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer userId;
    private String username;
    private String reason;
    private String status;
    private Date createTime;
}
