package com.xsx.jsoup.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:夏世雄
 * @Date: 2022/12/02/14:50
 * @Version: 1.0
 * @Discription:
 **/
@Configuration
@Setter
@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long expireTime;
}
