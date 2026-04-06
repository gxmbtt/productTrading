package com.example.producttrading.config;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.producttrading.entity.SysUser;
import com.example.producttrading.service.SysUserService;
import com.example.producttrading.utils.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author guoxin
 * @date 2026年04月06日 23:23
 */
@Component
public class JWTInterceptor implements HandlerInterceptor {
    @Autowired
    private SysUserService sysUserService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        //从请求头信息中获取token
        String token = request.getHeader("token");
        //如果请求头没有携带token，则从请求参数中获取
        if (StrUtil.isBlank(token)){
            token = request.getParameter("token");
        }
        //如果没有token，抛出401异常（没有权限）
        if (StrUtil.isBlank(token)){
            throw new CustomException(401, "您无权操作！");
        }

        //解析token中的载荷
        SysUser sysUser = null;
        try {
            String audience = JWT.decode(token).getAudience().get(0);
            sysUser = sysUserService.getById(Integer.parseInt(audience));
        } catch (Exception e) {
            throw new CustomException(401, "您无权操作！");
        }
        if (sysUser == null){
            throw new CustomException(401, "您无权操作！");
        }

        //验证签名
        try {
            JWT.require(Algorithm.HMAC256(sysUser.getPassword())).build().verify(token);
        } catch (Exception e) {
            throw new CustomException(401, "您无权操作！");
        }

        return true;
    }
}
