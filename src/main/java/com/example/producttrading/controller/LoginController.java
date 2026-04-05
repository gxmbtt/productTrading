package com.example.producttrading.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.producttrading.entity.SysUser;
import com.example.producttrading.service.SysUserService;
import com.example.producttrading.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author guoxin
 * @date 2026年04月05日 21:13
 */
@RestController
public class LoginController {
    @Autowired
    private SysUserService sysUserService;
    @PostMapping("/login")
    public Result login(@RequestBody Map<String,Object> map) {
        SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", map.get("username")));
        if (sysUser == null) {
            return Result.error("用户不存在");
        }
        if (!sysUser.getPassword().equals(map.get("password"))) {
            return Result.error("密码不正确");
        }
        return Result.success(sysUser);
    }
}
