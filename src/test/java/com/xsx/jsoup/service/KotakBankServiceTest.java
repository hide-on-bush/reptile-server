package com.xsx.jsoup.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @Author:夏世雄
 * @Date: 2022/09/09/13:31
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class KotakBankServiceTest {

    @Autowired
    private KotakBankService kotakBankService;

    @Test
    void login() throws Exception{
        kotakBankService.login();
    }


    @Test
    void test1() throws Exception{
        kotakBankService.get1();
    }

}