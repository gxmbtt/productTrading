package com.example.producttrading.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.producttrading.entity.SysUser;
import com.example.producttrading.service.SysUserService;
import com.example.producttrading.utils.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @RequestMapping("/export")
    public void export(HttpServletResponse response,SysUser sysUser) throws IOException {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.like(org.apache.commons.lang3.StringUtils.isNotEmpty(sysUser.getUsername()),"username",sysUser.getUsername());

        List<SysUser> list = sysUserService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true); // true表示创建xlsx格式
        writer.write(list,true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=test.xlsx");
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os,true);
        writer.close();
        IoUtil.close(os);
    }
    @PostMapping("/import")
    public Result importData(HttpServletResponse response,@RequestParam("file") MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        //调用Hutool中ExcelReader的readAll方法，将读取的数据封装成List集合
        List<SysUser> list = reader.readAll(SysUser.class);
        //批量保存到数据库
        Integer count = 0;
        for (SysUser item : list) {
            item.setUserId(null);
            item.setUsername(item.getUsername() + "_01");
            sysUserService.save(item);
            count ++;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("count",count);
        return Result.success(map);
    }
}
