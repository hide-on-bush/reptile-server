package com.xsx.jsoup.service;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/09/13/17:58
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class HarServiceTest {

    @Autowired
    HarService harService;

    @Test
    void har() throws Exception{
        System.out.println(harService.har());
    }

    @Test
    void har2() throws Exception{
        harService.har2();
    }
}