package com.xsx.jsoup.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/07/19/14:54
 * @Version: 1.0
 * @E-mail: xiashixiongtoxx@163.com
 * @Discription:
 **/
@SpringBootTest
class LogServiceTest {

    @Resource
    private LogService logService;

    @Test
    void getLogs()throws Exception {
        logService.getLogs();
    }
}