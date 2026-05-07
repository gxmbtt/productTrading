package com.example.producttrading.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.producttrading.entity.Apply;
import com.example.producttrading.entity.SysUser;
import com.example.producttrading.service.ApplyService;
import com.example.producttrading.service.SysUserService;
import com.example.producttrading.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/apply")
public class ApplyController {
    @Autowired
    private ApplyService applyService;
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/list")
    public Result list(Apply apply) {
        QueryWrapper<Apply> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        Page<Apply> page = Page.of(apply.getPageNum(), apply.getPageSize());
        List<Apply> list = applyService.list(page, queryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("list", list);
        return Result.success(map);
    }

    @GetMapping("/my")
    public Result my(HttpServletRequest request) {
        String token = request.getHeader("token");
        String userId = JWT.decode(token).getAudience().get(0);
        QueryWrapper<Apply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT 1");
        Apply apply = applyService.getOne(queryWrapper);
        return Result.success(apply);
    }

    @PostMapping
    public Result add(@RequestBody Apply apply, HttpServletRequest request) {
        String token = request.getHeader("token");
        String userId = JWT.decode(token).getAudience().get(0);
        SysUser sysUser = sysUserService.getById(Long.parseLong(userId));
        apply.setUserId(Integer.parseInt(userId));
        apply.setUsername(sysUser.getUsername());
        apply.setStatus("0");
        apply.setCreateTime(new Date());
        applyService.save(apply);
        return Result.success();
    }

    @PutMapping("/review")
    public Result review(@RequestBody Apply apply) {
        // 更新申请状态
        UpdateWrapper<Apply> applyWrapper = new UpdateWrapper<>();
        applyWrapper.eq("id", apply.getId());
        applyWrapper.set("status", apply.getStatus());
        applyService.update(applyWrapper);

        // 审核通过时，更新用户类型为助农用户
        if ("1".equals(apply.getStatus())) {
            Apply one = applyService.getById(apply.getId());
            UpdateWrapper<SysUser> userWrapper = new UpdateWrapper<>();
            userWrapper.eq("user_id", one.getUserId());
            userWrapper.set("type", "助农用户");
            sysUserService.update(userWrapper);
        }
        return Result.success();
    }
}
