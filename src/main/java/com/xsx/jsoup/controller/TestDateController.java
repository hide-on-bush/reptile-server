package com.xsx.jsoup.controller;

import com.xsx.jsoup.entity.TestDate;
import com.xsx.jsoup.service.TestDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:夏世雄
 * @Date: 2022/10/18/14:06
 * @Version: 1.0
 * @Discription:
 **/
@RestController
@RequestMapping("/api/test")
public class TestDateController {

    @Autowired
    private TestDateService testDateService;

    @GetMapping("/dates")
    public List<TestDate> getAll() {
        return testDateService.getAll();
    }
}
