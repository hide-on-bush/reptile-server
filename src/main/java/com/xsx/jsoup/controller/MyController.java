package com.xsx.jsoup.controller;

import com.xsx.jsoup.common.annotation.Prevent;
import com.xsx.jsoup.service.AppKeyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:夏世雄
 * @Date: 2022/10/09/17:54
 * @Version: 1.0
 * @Discription: 切面实现入参校验
 **/

@RestController
public class MyController {

    /**
     * 测试防刷
     *
     * @param mobile
     * @return
     */
    @GetMapping(value = "/testPrevent")
    @Prevent
    public String testPrevent(String mobile) {
        return "调用成功";
    }


    /**
     * 测试防刷
     *
     * @param mobile
     * @return
     */

    @GetMapping(value = "/testPreventIncludeMessage")
    @Prevent(message = "10秒内不允许重复调多次", value = "10")//value 表示10表示10秒
    public String testPreventIncludeMessage(String mobile) {
        return "调用成功";
    }

    @Autowired
    private AppKeyConfigService appKeyConfigService;

    @PostMapping("/app/update")
    public Boolean update(String channel, String message) {
        return appKeyConfigService.update(channel, message);
    }
}
