package com.xsx.jsoup.service;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

/**
 * @Author:夏世雄
 * @Date: 2022/11/01/13:55
 * @Version: 1.0
 * @Discription:
 **/
@Slf4j
@Service
public class MyBotService extends TelegramLongPollingBot {

    @Value("${telegram.token}")
    private String token;//= "5417845390:AAHuCLp_FlEFWF8XvNY57ahFhX_sQRGx9Jo";
    @Value("${telegram.username}")
    private String username;//="hyperBush_bot";

    @Autowired
    private UserService userService;

    public static DefaultBotOptions defaultBotOptions;

    private static final String PROXY_HOST = "127.0.0.1";
    private static final Integer PROXY_PORT = 7890;

    static {
        defaultBotOptions = new DefaultBotOptions();
        defaultBotOptions.setProxyHost(PROXY_HOST);
        defaultBotOptions.setProxyPort(PROXY_PORT);
        //ProxyType是个枚举
        defaultBotOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
    }

    public MyBotService() {
        this(defaultBotOptions);
    }

    public MyBotService(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            String text = message.getText();
            switch (text) {
                case "/a":
                    text = JSON.toJSONString(userService.getAll());
                    break;
                case "/b":
                    text = "BBBBBBBBBB";
                    break;
                case "/c":
                    text = "CCCCCCCCCC";
                    break;
                default:
                    text = "不处理该类指令";
                    break;
            }
            if (text.equals("不处理该类指令")) {
                this.sendFile(chatId, new File("C:\\Users\\Administrator\\Desktop\\各大银行收款短信.xls"), "hello");
            } else {
                this.sendTextMsg(text, chatId.toString());
            }

        }

    }

    public void sendFile(Long chatId, File file, String caption) {
        try {
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(String.valueOf(chatId));
            InputFile inputFile = new InputFile();
            inputFile.setMedia(file);
            sendDocument.setDocument(inputFile);
            sendDocument.setCaption(caption);
            Message message = execute(sendDocument);
            System.out.println(message);
        } catch (TelegramApiException e) {
            log.error("机器人发送文件失败，error={}", e.getMessage());
        }
    }

    /**
     * 发送文本消息
     *
     * @param text   内容
     * @param chatId 内容ID
     */
    @SneakyThrows
    @Async
    public void sendTextMsg(String text, String chatId) {
        SendMessage response = new SendMessage();
        response.setDisableNotification(false);
        response.setChatId(chatId);
        response.setText(text);
        executeAsync(response);
    }

}
