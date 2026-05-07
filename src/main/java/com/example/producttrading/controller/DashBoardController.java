package com.example.producttrading.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.producttrading.entity.Order;
import com.example.producttrading.entity.Products;
import com.example.producttrading.entity.SysUser;
import com.example.producttrading.service.OrderService;
import com.example.producttrading.service.ProductsService;
import com.example.producttrading.service.SysUserService;
import com.example.producttrading.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ProductsService productsService;

    @GetMapping("/today")
    public Result todayStats() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // 今日订单及营业额
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.apply("DATE(create_time) = {0}", today);
        List<Order> todayOrders = orderService.list(orderWrapper);
        BigDecimal revenue = BigDecimal.ZERO;
        for (Order order : todayOrders) {
            try {
                BigDecimal price = new BigDecimal(order.getPrice());
                revenue = revenue.add(price.multiply(BigDecimal.valueOf(order.getCount())));
            } catch (Exception ignored) {}
        }

        // 今日新增用户
        QueryWrapper<SysUser> userWrapper = new QueryWrapper<>();
        userWrapper.apply("DATE(create_time) = {0}", today);
        long newUserCount = sysUserService.count(userWrapper);

        // 今日新增农产品
        QueryWrapper<Products> productWrapper = new QueryWrapper<>();
        productWrapper.apply("DATE(create_time) = {0}", today);
        long newProductCount = productsService.count(productWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("revenue", revenue);
        map.put("orderCount", todayOrders.size());
        map.put("newUserCount", newUserCount);
        map.put("newProductCount", newProductCount);
        return Result.success(map);
    }

    @GetMapping("/weekSales")
    public Result weekSales() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dates = new ArrayList<>();
        List<BigDecimal> sales = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            Calendar temp = Calendar.getInstance();
            temp.add(Calendar.DAY_OF_MONTH, -i);
            String date = sdf.format(temp.getTime());
            dates.add(date.substring(5));

            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.apply("DATE(create_time) = {0}", date);
            List<Order> orders = orderService.list(wrapper);
            BigDecimal dayTotal = BigDecimal.ZERO;
            for (Order order : orders) {
                try {
                    BigDecimal price = new BigDecimal(order.getPrice());
                    dayTotal = dayTotal.add(price.multiply(BigDecimal.valueOf(order.getCount())));
                } catch (Exception ignored) {}
            }
            sales.add(dayTotal);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("dates", dates);
        map.put("sales", sales);
        return Result.success(map);
    }

    @GetMapping("/category")
    public Result categoryStats() {
        List<Map<String, Object>> list = productsService.listMaps(
            new QueryWrapper<Products>()
                .select("category as name", "count(*) as value")
                .eq("deleted", 0)
                .groupBy("category")
        );
        return Result.success(list);
    }

    @GetMapping("/weekUsers")
    public Result weekUsers() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            Calendar temp = Calendar.getInstance();
            temp.add(Calendar.DAY_OF_MONTH, -i);
            String date = sdf.format(temp.getTime());
            dates.add(date.substring(5));

            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.apply("DATE(create_time) = {0}", date);
            counts.add(sysUserService.count(wrapper));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("dates", dates);
        map.put("counts", counts);
        return Result.success(map);
    }
}
