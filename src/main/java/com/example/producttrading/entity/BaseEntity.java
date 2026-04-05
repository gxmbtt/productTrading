package com.example.producttrading.entity;

import cn.hutool.core.annotation.PropIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author guoxin
 * @date 2026年04月04日 13:34
 */
@Data
public class BaseEntity implements Serializable {
    @JsonIgnore
    @PropIgnore
    @TableField(exist = false)
    private Integer pageNum = 1;
    @JsonIgnore
    @PropIgnore
    @TableField(exist = false)
    private Integer pageSize = 10;
}
