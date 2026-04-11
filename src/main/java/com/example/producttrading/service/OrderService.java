package com.example.producttrading.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.producttrading.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author guoxin
 * @date 2026年04月12日 0:37
 */
public interface OrderService extends IService<Order> {
    public Double selectDailyMoney(@Param("begin") Date begin, @Param("end") Date end);

    public List<Map<String,Object>> selectMonthMoney(@Param("begin") Date begin, @Param("end") Date end);
}
