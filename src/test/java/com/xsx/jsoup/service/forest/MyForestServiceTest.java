package com.xsx.jsoup.service.forest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/12/05/14:18
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class MyForestServiceTest {

    @Autowired
    MyForestService myForestService;

    @Test
    void getLocation() {
        Map location = myForestService.getLocation("116.41339", "39.91092");
        location.forEach((k, v) -> {
            System.out.println("k=" + k + ";v=" + v);
        });
    }
}