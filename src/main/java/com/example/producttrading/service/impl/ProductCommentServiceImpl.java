package com.example.producttrading.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.producttrading.entity.ProductComment;
import com.example.producttrading.entity.Products;
import com.example.producttrading.mapper.ProductCommentMapper;
import com.example.producttrading.mapper.ProductsMapper;
import com.example.producttrading.service.ProductCommentService;
import com.example.producttrading.service.ProductsService;
import org.springframework.stereotype.Service;

/**
 * @author guoxin
 * @date 2026年04月12日 0:14
 */
@Service
public class ProductCommentServiceImpl extends ServiceImpl<ProductCommentMapper, ProductComment> implements ProductCommentService {
}
