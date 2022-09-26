package com.xsx.jsoup.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author:夏世雄
 * @Date: 2022/09/07/16:34
 * @Version: 1.0
 * @Discription:
 **/
@Service
@Slf4j
public class RedisService {

    @Autowired
    StringRedisTemplate redisTemplate;

    public void read(){
        String otp = null;
        while(StringUtils.isBlank(otp)) {
            log.info("阻塞读取otp={}", otp);
            otp = (String)redisTemplate.opsForHash().get("otp","hyperbush@proto.me");
        }
        System.out.println(otp);
    }

    public void set(){
        redisTemplate.opsForHash().put("otp", "hyperbush123@proto.me", "123456");
        redisTemplate.opsForHash().put("otp", "hyperbush@proto.me", "888888");
        redisTemplate.opsForHash().put("otp", "hyperbush123@outlook.com", "999999");
        redisTemplate.opsForHash().put("otp", "hyperbush@proto.me", "666666");
    }

    public void bRead(){
        System.out.println("阻塞读");
        Object o = redisTemplate.opsForHash().get("admin", "name");
        System.out.println(o);
        String hello = redisTemplate.opsForList().leftPop("hello", 30, TimeUnit.SECONDS);
        System.out.println(hello);
    }

    public void write(){
        redisTemplate.opsForHash().put("admin", "name", "root");
        redisTemplate.opsForList().leftPush("hello", "world");
    }
}
