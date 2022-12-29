package com.xsx.jsoup.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/10/27/15:47
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class AppKeyConfigServiceTest {

    @Autowired
    AppKeyConfigService appKeyConfigService;

    @Test
    void getByKeyWord() {
        System.out.println(appKeyConfigService.getByKeyWord().getValue());
    }

    @Test
    void update() {
        String chanel = "HELLO";
        String message = "{\"code\":90415,\"message\":\"Operation failed, please contact[] customer \\service to !@report the problem\",\"success\":false,\"msg\":\"Operation failed, please contact customer service to report the problem\"}";
        System.out.println(appKeyConfigService.update(chanel, message));
    }
}