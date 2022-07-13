package com.xsx.jsoup.controller;

import com.xsx.jsoup.service.OkHttpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/ok-http")
public class OkHttpClientController {

    @Autowired
    private OkHttpClientService okHttpClientService;

    @GetMapping("/execute")
    public String syncNetAccess(){
        return okHttpClientService.syncGet();
    }

    @GetMapping("/enqueue")
    public void asyncNetAccess() throws IOException {
        okHttpClientService.asyncGet();
    }
}
