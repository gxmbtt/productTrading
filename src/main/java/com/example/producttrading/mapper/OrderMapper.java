package com.example.producttrading.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.producttrading.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT sum(price * `count`) from r_order where create_time between #{begin} and #{end}")
    public Double selectDailyMoney(@Param("begin") Date begin, @Param("end") Date end);

    @Select("SELECT DATE_FORMAT(create_time,'%Y-%m-%d') as order_date, sum(price * `count`) as m from r_order where create_time between #{begin} and #{end} GROUP BY DATE_FORMAT(create_time,'%Y-%m-%d') order by order_date asc")
    public List<Map<String,Object>> selectMonthMoney(@Param("begin") Date begin, @Param("end") Date end);
}

