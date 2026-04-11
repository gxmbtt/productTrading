package com.example.producttrading.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.producttrading.entity.Products;
import com.example.producttrading.mapper.ProductsMapper;
import com.example.producttrading.service.ProductsService;
import com.example.producttrading.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author guoxin
 * @date 2026年04月08日 18:05
 */
@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products> implements ProductsService {
    @Autowired
    private SysUserService sysUserService;
    @Override
    public List<Products> list(IPage<Products> page, Wrapper<Products> queryWrapper) {
        List<Products> list = super.list(page, queryWrapper);

        list.forEach(item -> {
            item.setSysUser(sysUserService.getById(item.getUserId()));
        });
        return list;
    }
}
