package com.example.producttrading.service;

import com.example.producttrading.entity.Products;

import java.util.List;

public interface RecommendationService {
    List<Products> recommend(Long userId, int size);
    List<Products> hotRecommend(int size);
}
