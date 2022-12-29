package com.xsx.jsoup.service;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author:夏世雄
 * @Date: 2022/07/22/14:36
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
public class RedisTest {

    @Autowired
    @Qualifier("myRedisTemplate")
    private RedisTemplate redisTemplate;


    @Test
    void test() throws Exception {
        Thread.sleep(300000);
        redisTemplate.opsForValue().set("helloadsasda", "world  ADSsD");

    }

    @Test
    void testRedis() throws Exception {
        String bankUser = "java";
        String headers = "hide_on_bush";
        redisTemplate.opsForValue().set("hello", "wrold");
        Executors.newFixedThreadPool(1).submit(() -> {
            System.out.println("hello");
            redisTemplate.opsForValue().set(bankUser, headers, 30L, TimeUnit.MINUTES);
            System.out.println("***************************************************");
        });
    }

    @Test
    void test2() {
        redisTemplate.opsForValue().set("aaa", "123");
        new Thread(new Runnable() {
            public void run() {
                System.out.println("========================hello==================");
                redisTemplate.opsForValue().set("ADMIN", "ROOT");
                System.out.println("-------------");
            }
        }).start();

    }
}
