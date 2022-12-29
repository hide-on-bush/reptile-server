package com.xsx.jsoup.service;

import org.springframework.stereotype.Service;

/**
 * @Author:夏世雄
 * @Date: 2022/10/17/18:10
 * @Version: 1.0
 * @Discription:
 **/
@Service
public class UseAopService {

    public String m1() {
        return "use aop method1";
    }

    public String m2(int a) {
        return String.valueOf(a);
    }

    public void m3() {
        System.out.println("我是m3方法");
    }
}
