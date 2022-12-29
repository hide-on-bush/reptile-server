package com.xsx.jsoup.controller;

import com.xsx.jsoup.service.UseAopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:夏世雄
 * @Date: 2022/10/17/18:13
 * @Version: 1.0
 * @Discription:
 **/
@RestController
@RequestMapping("/api/aop/")
public class UseAopController {

    private UseAopService useAopService;

    public UseAopController(UseAopService useAopService) {
        this.useAopService = useAopService;
    }

    @GetMapping("m1")
    public String method1() {
        return useAopService.m1();
    }

    @GetMapping("m2/{age}")
    public String method2(@PathVariable int age) {
        return useAopService.m2(age);
    }

}
