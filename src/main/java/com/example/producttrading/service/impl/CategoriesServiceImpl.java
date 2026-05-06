package com.example.producttrading.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.producttrading.entity.Categories;
import com.example.producttrading.mapper.CategoriesMapper;
import com.example.producttrading.service.CategoriesService;
import org.springframework.stereotype.Service;

/**
 * @author guoxin
 * @date 2026年05月05日 0:07
 */
@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories> implements CategoriesService {
}
