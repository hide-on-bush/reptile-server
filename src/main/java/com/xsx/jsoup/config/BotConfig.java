package com.xsx.jsoup.config;


import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * @Author:夏世雄
 * @Date: 2022/11/01/13:52
 * @Version: 1.0
 * @Discription:
 **/
@Configuration
public class BotConfig {

    @SneakyThrows
    @Bean
    public TelegramBotsApi telegramBotsApi() {
        DefaultBotSession defaultBotSession = new DefaultBotSession();
        return new TelegramBotsApi(defaultBotSession.getClass());
    }
}

