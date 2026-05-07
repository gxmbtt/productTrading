package com.example.producttrading.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.producttrading.entity.Notice;
import com.example.producttrading.entity.SysUser;
import com.example.producttrading.service.NoticeService;
import com.example.producttrading.service.SysUserService;
import com.example.producttrading.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/list")
    public Result list(Notice notice) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        Page<Notice> page = Page.of(notice.getPageNum(), notice.getPageSize());
        List<Notice> list = noticeService.list(page, queryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("list", list);
        return Result.success(map);
    }

    @PostMapping
    public Result add(@RequestBody Notice notice, HttpServletRequest request) {
        notice.setCreateTime(new Date());
        String token = request.getHeader("token");
        if (token != null) {
            String audience = JWT.decode(token).getAudience().get(0);
            SysUser sysUser = sysUserService.getById(Long.parseLong(audience));
            if (sysUser != null) {
                notice.setCreateUser(sysUser.getNickname());
            }
        }
        noticeService.save(notice);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable Long[] ids) {
        noticeService.removeByIds(Arrays.asList(ids));
        return Result.success();
    }
}
