package com.example.producttrading.controller;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.producttrading.entity.Order;
import com.example.producttrading.service.OrderService;
import com.example.producttrading.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guoxin
 * @date 2026年04月12日 0:51
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public Result add(@RequestBody List<Order> orders){
        orderService.saveBatch(orders);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list(HttpServletRequest request, Order order){

        //添加条件：登录用户  时间段查询  状态
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        String token = request.getHeader("token");
        String userId = JWT.decode(token).getAudience().get(0);
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq(order.getStatus() != null && !"-1".equals(order.getStatus()),"status",order.getStatus());
     //   queryWrapper.ge(StrUtil.isNotBlank(order.getParams().get("begin")),"create_time",order.getParams().get("begin"));
     //   queryWrapper.le(StrUtil.isNotBlank(order.getParams().get("end")),"create_time",order.getParams().get("end") + " 23:59:59");

        //设置分页
        Page<Order> page = Page.of(order.getPageNum(), order.getPageSize());
        List<Order> list = orderService.list(page, queryWrapper);

        Map<String,Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("list", list);

        return Result.success(map);
    }
}

