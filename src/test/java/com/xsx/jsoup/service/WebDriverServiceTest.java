package com.xsx.jsoup.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebDriverServiceTest {

    @Autowired
    private WebDriverService webDriverService;

    @Test
    void useWebDriver() throws Exception{
        webDriverService.useWebDriver();
    }

    @Test
    void testGetHeaders() throws Exception{
        webDriverService.getHeaders();
    }
}