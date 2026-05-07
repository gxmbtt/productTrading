package com.example.producttrading.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.producttrading.entity.Notice;
import com.example.producttrading.mapper.NoticeMapper;
import com.example.producttrading.service.NoticeService;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
}
