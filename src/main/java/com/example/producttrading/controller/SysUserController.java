package com.example.producttrading.controller;

import com.example.producttrading.entity.SysUser;
import com.example.producttrading.service.SysUserService;
import com.example.producttrading.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result list() {
        return Result.success(sysUserService.list());
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
