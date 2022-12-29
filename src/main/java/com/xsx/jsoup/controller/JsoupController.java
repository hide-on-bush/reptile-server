package com.xsx.jsoup.controller;

import com.xsx.jsoup.service.JsoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jsoup")
public class JsoupController {

    @Autowired
    private JsoupService jsoupService;

    @GetMapping("/connection")
    public String jsoupConnection() {
        return jsoupService.jsoupConnection();
    }

    @GetMapping("/open")
    public String littleRedBook() throws Exception {
        return jsoupService.littleRedBook();
    }
}
