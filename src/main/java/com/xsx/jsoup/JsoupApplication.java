package com.xsx.jsoup;

import com.thebeastshop.forest.springboot.annotation.ForestScan;
import com.xsx.jsoup.service.MyBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetAddress;

@Slf4j
@EnableScheduling
// forest扫描远程接口所在的包名
@ForestScan(basePackages = "com.xsx.jsoup.service.forest")
@SpringBootApplication
public class JsoupApplication implements CommandLineRunner {

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private TelegramBotsApi telegramBotsApi;

    @Autowired
    private MyBotService myBotService;

    public static void main(String[] args) {
        SpringApplication.run(JsoupApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        telegramBotsApi.registerBot(myBotService);
        InetAddress address = InetAddress.getLocalHost();
        String ip = address.getHostAddress();
        String hostName = address.getHostName();
        log.info("jsoup Started! [{}<{}>].", hostName, ip);
    }


}
