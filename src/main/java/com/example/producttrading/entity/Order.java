package com.example.producttrading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author guoxin
 * @date 2026年04月12日 0:20
 */
@Data
@TableName("r_order")
public class Order extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderId;
    private long productId;
    private String name;
    private String image;
    private long count;
    private String price;
    private String unit;
    private String linkUser;
    private String linkAddress;
    private String linkPhone;
    private String status;
    private Date createTime;
    private long userId;

}
