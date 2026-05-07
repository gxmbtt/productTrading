package com.example.producttrading.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.producttrading.entity.Order;
import com.example.producttrading.entity.Products;
import com.example.producttrading.mapper.CartMapper;
import com.example.producttrading.mapper.OrderMapper;
import com.example.producttrading.mapper.ProductsMapper;
import com.example.producttrading.service.OrderService;
import com.example.producttrading.utils.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author guoxin
 * @date 2026年04月12日 1:13
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private ProductsMapper productsMapper;
    @Autowired
    private CartMapper cartMapper;
    @Override
    public Double selectDailyMoney(Date begin, Date end) {
        return baseMapper.selectDailyMoney(begin,end);
    }

    @Override
    public List<Map<String, Object>> selectMonthMoney(Date begin, Date end) {
        return baseMapper.selectMonthMoney(begin, end);
    }

    @Override
    public boolean saveBatch(Collection<Order> entityList) {
        //获取要删除的购物车条目的编号
        List<Long> ids = entityList.stream().map(item -> item.getId()).collect(Collectors.toList());
        //判断所购买的农产品库存是否充足，如果不充足，下单失败
        entityList.forEach(order -> {
            Products products = productsMapper.selectById(order.getProductId());
            if (products.getStock() - order.getCount() < 0){
                throw new CustomException(500, "农产品库存不足，不能下单！");
            }
        });
        //库存充足则下单（每个产品生成一个订单）：订单号按照规范生成：用户编号+时间戳
        entityList.forEach(item -> {
            String orderId = item.getUserId() + DateUtil.format(new Date(),"yyyyMMddHHmm");
            item.setOrderId(orderId);
            item.setId(null);
            baseMapper.insert(item);
        });
        //更新商品库存和销量
        entityList.forEach(item -> {
            productsMapper.updateStock(item.getProductId(), item.getCount());
            UpdateWrapper<Products> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", item.getProductId());
            wrapper.setSql("sell_count = sell_count + " + item.getCount());
            productsMapper.update(null, wrapper);
        });
        //并且清空购物车

        cartMapper.deleteByIds(ids);
        return true;
    }
}
