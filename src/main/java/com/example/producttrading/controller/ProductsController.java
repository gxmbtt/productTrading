package com.example.producttrading.controller;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.producttrading.entity.Products;
import com.example.producttrading.service.ProductsService;
import com.example.producttrading.service.SysUserService;
import com.example.producttrading.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guoxin
 * @date 2026年04月10日 16:08
 */
@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductsService productsService;
    @Autowired
    private SysUserService sysUserService;

 /*   @GetMapping("/recommendList")
    public Result recommendList(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = null;
        if (token != null){
            String s = JWT.decode(token).getAudience().get(0);
            userId = Long.parseLong(s);
        }
        return Result.success(productsService.selectRecommendList(userId,4));
    }
*/
    @GetMapping("/list")
    public Result list(HttpServletRequest request, Products products){
        //设置查询条件
        QueryWrapper<Products> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(products.getName()), "name", products.getName());
        queryWrapper.eq(StrUtil.isNotBlank(products.getCategory()),"category", products.getCategory());

        //获取登录用户的编号, 从token中获取
//        String token = request.getHeader("token");
//        if (StrUtil.isNotBlank(token)){
//            String audience = JWT.decode(token).getAudience().get(0);
//            SysUser sysUser = sysUserService.getById(Integer.parseInt(audience));
//            if (sysUser.getType().equals("普通用户")){
//                queryWrapper.eq("user_id", sysUser.getUserId());
//            }
//        }

        //设置排序
 //       queryWrapper.orderBy(StrUtil.isNotBlank(products.getColumn()),"asc".equalsIgnoreCase(products.getSort()),products.getColumn());

        //设置分页
        Page<Products> page = Page.of(products.getPageNum(), products.getPageSize());
        List<Products> list = productsService.list(page, queryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("list", list);
        return Result.success(map);
    }

    @PostMapping
    public Result add(@RequestBody Products products){
        productsService.save(products);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Products products){
        productsService.updateById(products);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id){
        return Result.success(productsService.getById(id));
    }

    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable Long[] ids){
        productsService.removeByIds(Arrays.asList(ids));
        return Result.success();
    }
}
