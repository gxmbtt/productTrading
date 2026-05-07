package com.example.producttrading.controller;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.producttrading.entity.SysUserAddress;
import com.example.producttrading.service.SysUserAddressService;
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
@RequestMapping("/address")
public class SysUserAddressController {

    @Autowired
    private SysUserAddressService addressService;

    @GetMapping("/list")
    public Result list(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            return Result.error("请先登录");
        }
        String audience = JWT.decode(token).getAudience().get(0);
        Long userId = Long.parseLong(audience);
        QueryWrapper<SysUserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return Result.success(addressService.list(queryWrapper));
    }

    @PostMapping
    public Result add(@RequestBody SysUserAddress address, HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            return Result.error("请先登录");
        }
        String audience = JWT.decode(token).getAudience().get(0);
        address.setUserId(Long.parseLong(audience));
        addressService.save(address);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody SysUserAddress address) {
        addressService.updateById(address);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        addressService.removeById(id);
        return Result.success();
    }
}
