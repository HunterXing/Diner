package com.aiit.diner.utils;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author xingheng
 */
@Slf4j
@Component
public class JwtUtils {

    private static String salt;
    private static int expireTime;

    /**
     * static 属性进行@Value初始化时，需要使用set方法
     */
    @Value("${JWT.salt}")
    public void setSalt(String salt) {
        JwtUtils.salt = salt;
    }

    @Value("${JWT.expireTime}")
    public void setExpireTime(int expireTime) {
        JwtUtils.expireTime = expireTime * 1000 * 60;
    }

    public static String generateToken(Long employeeId) {
        log.info("generateToken, 盐：{}",  salt);
        log.info("generateToken, 过期时间：{}",  expireTime);

        //默认使用HS265(HmacSHA256)算法
        byte [] key = salt.getBytes();
        return JWT.create()
                .setPayload("id", employeeId)
                .setExpiresAt(new Date(System.currentTimeMillis()+ expireTime * DateUnit.MINUTE.getMillis()))
                .setIssuedAt(new Date())
                .setSigner(JWTSignerUtil.hs256(key))
                .sign();
    }

    /**
     * 通过token获取用户id
     * @return Long userId
     */
    public static Long getUserIdByToken(String token) {
        final JWT jwt = JWTUtil.parseToken(token);
        log.info("jwt, {}",  jwt);
        return  Convert.toLong(jwt.getPayload("id"));
    }
}