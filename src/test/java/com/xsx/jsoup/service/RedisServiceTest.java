package com.xsx.jsoup.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/09/07/16:38
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class RedisServiceTest {

    @Autowired
    RedisService redisService;

    @Test
    void read() {
        redisService.read();
    }


    @Test
    void set() {
        redisService.set();
    }


    @Test
    public void bRead() {
        System.out.println(StringUtils.isBlank(""));
        redisService.bRead();
    }

    @Test
    public void write(){
        redisService.write();
    }
}