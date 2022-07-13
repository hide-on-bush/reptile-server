package com.xsx.jsoup.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/07/13/10:18
 * @Version: 1.0
 * @E-mail: xiashixiongtoxx@163.com
 * @Discription:
 **/

@SpringBootTest
class BankCardServiceTest {

    @Autowired
    private BankCardService bankCardService;

    @Test
    void login() {
        bankCardService.login();
    }
}