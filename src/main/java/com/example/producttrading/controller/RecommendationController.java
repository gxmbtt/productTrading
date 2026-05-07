package com.example.producttrading.controller;

import com.auth0.jwt.JWT;
import com.example.producttrading.entity.Products;
import com.example.producttrading.service.RecommendationService;
import com.example.producttrading.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public Result recommend(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = null;
        if (token != null) {
            userId = Long.parseLong(JWT.decode(token).getAudience().get(0));
        }
        List<Products> list;
        if (userId != null) {
            list = recommendationService.recommend(userId, 4);
        } else {
            list = recommendationService.hotRecommend(4);
        }
        return Result.success(list);
    }

    @GetMapping("/hot")
    public Result hot() {
        List<Products> list = recommendationService.hotRecommend(4);
        return Result.success(list);
    }
}
