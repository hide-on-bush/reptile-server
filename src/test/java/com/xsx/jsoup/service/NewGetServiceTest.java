package com.xsx.jsoup.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

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
    void getBalance() throws Exception {
        newGetService.getBalance();
    }

    @Test
    void testRest() {
        newGetService.sentByRest(new HashMap<>());
    }

    @Test
    void test() {
        String cookie = "BIGipServerwww.ktbnetbank.com_ext=!AVfKz09/cgKcmX7jxlhocsrvj8acgW6hUdB582kVpeYYyStw04B75gxIOksiLScfkJdugq2CKqFrvRg=; expires=Fri, 12-Aug-2022 09:17:23 GMT; path=/; Httponly; Secure; Secure; HttpOnly";
        if (cookie.contains("12-Aug-2022")) {
            cookie = cookie.replace("12-Aug-2022", "13-Aug-2022");
        }
        System.out.println(cookie);
    }
}