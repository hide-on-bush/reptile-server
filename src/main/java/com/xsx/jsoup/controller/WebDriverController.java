package com.xsx.jsoup.controller;

import com.xsx.jsoup.service.WebDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web-driver")
public class WebDriverController {

    @Autowired
    private WebDriverService webDriverService;

    @GetMapping("/use")
    public void useWebDriver() throws Exception{
        webDriverService.useWebDriver();
    }
}
