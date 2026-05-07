package com.example.producttrading.controller;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.producttrading.entity.Cart;
import com.example.producttrading.service.CartService;
import com.example.producttrading.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author guoxin
 * @date 2026年05月07日
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public Result add(@RequestBody Cart cart, HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            return Result.error("请先登录");
        }
        String audience = JWT.decode(token).getAudience().get(0);
        Long userId = Long.parseLong(audience);
        cart.setUserId(userId);
        cart.setCreateTime(new Date());
        cartService.save(cart);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            return Result.error("请先登录");
        }
        String audience = JWT.decode(token).getAudience().get(0);
        Long userId = Long.parseLong(audience);
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        return Result.success(cartService.list(queryWrapper));
    }

    @PutMapping
    public Result update(@RequestBody Cart cart, HttpServletRequest request) {
        String token = request.getHeader("token");
        String updateUser = null;
        if (StrUtil.isNotBlank(token)) {
            String audience = JWT.decode(token).getAudience().get(0);
            updateUser = audience;
        }
        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", cart.getId());
        updateWrapper.set("count", cart.getCount());
        updateWrapper.set("update_time", new Date());
        updateWrapper.set("update_user", updateUser);
        cartService.update(updateWrapper);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        cartService.removeById(id);
        return Result.success();
    }
}
