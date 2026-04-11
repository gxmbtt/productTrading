package com.example.producttrading.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.producttrading.entity.Cart;
import com.example.producttrading.mapper.CartMapper;
import com.example.producttrading.service.CartService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guoxin
 * @date 2026年04月12日 1:12
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Override
    public boolean save(Cart entity) {
        //判断用户是否已经将该商品加入了购物车，如果已经加入，则更新数量，否则添加购物车
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",entity.getUserId());
        map.put("goods_id",entity.getGoodsId());
        List<Cart> list = baseMapper.selectByMap(map);

        if (list == null || list.size() == 0){
            baseMapper.insert(entity);
        }

        Cart cart = list.get(0);
        cart.setCount(cart.getCount() + entity.getCount());
        return baseMapper.updateById(cart) > 0;
    }
}

