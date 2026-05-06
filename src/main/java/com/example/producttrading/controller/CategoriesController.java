package com.example.producttrading.controller;

import com.example.producttrading.entity.Categories;
import com.example.producttrading.service.CategoriesService;
import com.example.producttrading.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

/**
 * @author guoxin
 * @date 2026年05月05日 0:10
 */
@RestController
@RequestMapping("/categories")
public class CategoriesController {
    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("/all")
    public Result listAll(){
        return Result.success(categoriesService.list());
    }

    @PostMapping
    public Result add(@RequestBody Categories categories){
        categories.setCreateTime(new Date());
        categories.setUpdateTime(new Date());
        categories.setDeleted(0L);
        categoriesService.save(categories);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Categories categories){
        categories.setUpdateTime(new Date());
        categoriesService.updateById(categories);
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable Long[] ids){
        categoriesService.removeByIds(Arrays.asList(ids));
        return Result.success();
    }
}
