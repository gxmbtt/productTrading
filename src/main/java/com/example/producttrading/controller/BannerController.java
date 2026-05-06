package com.example.producttrading.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.producttrading.entity.Banner;
import com.example.producttrading.entity.SysUser;
import com.example.producttrading.service.BannerService;
import com.example.producttrading.service.SysUserService;
import com.example.producttrading.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/banners")
public class BannerController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/list")
    public Result list(Banner banner) {
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        Page<Banner> page = Page.of(banner.getPageNum(), banner.getPageSize());
        List<Banner> list = bannerService.list(page, queryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("list", list);
        return Result.success(map);
    }

    @PostMapping
    public Result add(@RequestBody Banner banner, HttpServletRequest request) {
        banner.setCreateTime(new Date());
        String token = request.getHeader("token");
        if (token != null) {
            String audience = JWT.decode(token).getAudience().get(0);
            SysUser sysUser = sysUserService.getById(Long.parseLong(audience));
            if (sysUser != null) {
                banner.setCreateUser(sysUser.getNickname());
            }
        }
        bannerService.save(banner);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Banner banner) {
        bannerService.updateById(banner);
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable Long[] ids) {
        bannerService.removeByIds(Arrays.asList(ids));
        return Result.success();
    }
}
