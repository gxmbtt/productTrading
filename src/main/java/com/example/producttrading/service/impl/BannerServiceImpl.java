package com.example.producttrading.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.producttrading.entity.Banner;
import com.example.producttrading.mapper.BannerMapper;
import com.example.producttrading.service.BannerService;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
}
