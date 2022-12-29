package com.xsx.jsoup.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/07/20/13:03
 * @Version: 1.0
 * @E-mail: xiashixiongtoxx@163.com
 * @Discription:
 **/
@SpringBootTest
class XiaoHongShuTest {

    @Autowired
    private XiaoHongShu xiaoHongShu;

    @Test
    void login() throws Exception {
        xiaoHongShu.test();
    }
}