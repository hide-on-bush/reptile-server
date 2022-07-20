package com.xsx.jsoup.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/07/15/10:34
 * @Version: 1.0
 * @E-mail: xiashixiongtoxx@163.com
 * @Discription:
 **/
@SpringBootTest
class NewGetServiceTest {

    @Autowired
    private NewGetService newGetService;

    @Test
    void getHeader() {
        System.out.println(newGetService.login());
    }

    //此方法用于测试idfc获取银行卡余额
    @Test
    void getBalance() throws Exception{
        newGetService.getBalance();
    }
}