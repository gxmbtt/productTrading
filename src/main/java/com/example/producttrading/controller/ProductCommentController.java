package com.example.producttrading.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.producttrading.entity.ProductComment;
import com.example.producttrading.service.ProductCommentService;
import com.example.producttrading.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guoxin
 * @date 2026年04月12日 0:22
 */
@RestController
@RequestMapping("/comment")
public class ProductCommentController {
    @Autowired
    private ProductCommentService productCommentService;

    @GetMapping("/all")
    public Result list(ProductComment comment){
        return Result.success(productCommentService.list(new QueryWrapper<ProductComment>().eq("pid",comment.getPid())));
    }
}
