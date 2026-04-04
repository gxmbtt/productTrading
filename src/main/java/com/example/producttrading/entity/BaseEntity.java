package com.example.producttrading.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author guoxin
 * @date 2026年04月04日 13:34
 */
@Data
public class BaseEntity implements Serializable {
    @TableField(exist = false)
    private Integer pageNum = 1;
    @TableField(exist = false)
    private Integer pageSize = 10;
}
