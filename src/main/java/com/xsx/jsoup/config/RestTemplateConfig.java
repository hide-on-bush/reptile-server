package com.xsx.jsoup.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @Author:夏世雄
 * @Date: 2022/07/20/15:32
 * @Version: 1.0
 * @E-mail: xiashixiongtoxx@163.com
 * @Discription:
 **/
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(@Qualifier("simpleClientHttpRequestFactory") SimpleClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
        //配置restTemplate的代理
        Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("192.168.1.6", 4780));
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(50 * 1000); //毫秒
        factory.setReadTimeout(50 * 1000); //毫秒
        factory.setProxy(proxy);
        return factory;
    }
}

