package com.example.producttrading.utils;

import cn.hutool.core.date.DateUtil;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

/**
 * @author guoxin
 * @date 2026年04月06日 18:54
 */
public class TokenUtils {
    public static String createToken(String data, String sign){
        return JWT.create().withAudience(data) //将user-role保存到token中个，作为载荷
                .withExpiresAt(DateUtil.offsetDay(new Date(),1)) //设置token过期时间为1天
                .sign(Algorithm.HMAC256(sign)); //以password作为token的秘钥，HMAC256算法进行加密
    }
}
