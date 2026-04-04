package com.example.producttrading.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.producttrading.entity.SysUser;
import com.example.producttrading.service.SysUserService;
import com.example.producttrading.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guoxin
 * @date 2026年04月03日 14:39
 */
@RestController
@RequestMapping("/sysuser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/list")
    public Result list(SysUser sysUser) {
        //添加查询条件
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.hasText(sysUser.getUsername()),"username",sysUser.getUsername());
        wrapper.eq(StringUtils.hasText(sysUser.getType()),"type",sysUser.getType());

        //分页设置
        Page<SysUser> page = Page.of(sysUser.getPageNum(),sysUser.getPageSize());
        List<SysUser> list = sysUserService.list(page, wrapper);

        //组织返回的数据
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("total",page.getTotal());

        return Result.success(map);
    }


    @PostMapping
    public Result save(@RequestBody SysUser sysUser) {
        return sysUserService.save(sysUser) ? Result.success() : Result.error("操作失败");
    }

    @PutMapping
    public Result update(@RequestBody SysUser sysUser) {
        return sysUserService.updateById(sysUser) ? Result.success() : Result.error("操作失败");
    }
    @GetMapping("/{id}")
    public Result get(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return sysUserService.removeById(id) ? Result.success() : Result.error("操作失败");
    }
}
