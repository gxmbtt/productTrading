package com.example.producttrading.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.producttrading.entity.SysUser;
import com.example.producttrading.mapper.SysUserMapper;
import com.example.producttrading.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author guoxin
 * @date 2026年04月03日 14:48
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public boolean save(SysUser entity) {
        //对密码进行md5算法加密
        entity.setPassword(DigestUtils.md5DigestAsHex(entity.getPassword().getBytes()));
        return super.save(entity);
    }
}
