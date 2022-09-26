package com.xsx.jsoup.service.Kotak;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/09/14/10:37
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Test
    void login() throws Exception{
        loginService.getToken();
        //loginService.login();
    }
}