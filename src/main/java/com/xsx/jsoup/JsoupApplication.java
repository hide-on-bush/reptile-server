package com.xsx.jsoup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;

@Slf4j
@SpringBootApplication
public class JsoupApplication implements CommandLineRunner {

    @Value("${server.port}")
    private String serverPort;

    public static void main(String[] args) {
        SpringApplication.run(JsoupApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        InetAddress address = InetAddress.getLocalHost();
        String ip = address.getHostAddress();
        String hostName = address.getHostName();
        log.info("jsoup Started! [{}<{}>].", hostName, ip);
    }
}
