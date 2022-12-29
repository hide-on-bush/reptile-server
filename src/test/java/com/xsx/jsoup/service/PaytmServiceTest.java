package com.xsx.jsoup.service;

import com.xsx.jsoup.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/08/26/14:40
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class PaytmServiceTest extends BaseTest {

    @Autowired
    PaytmService paytmService;

    @Test
    void login() throws Exception {
        paytmService.login(initBankUser());
    }

    @Test
    void test1() {
        String signalBody = "[{\"deviceDateTime\":" + System.currentTimeMillis() + ",\"eventType\":\"custom_event\",\"appVersion\":\"1.0.0\",\"sdkVersion\":\"2.2.0\",\"clientId\":\"oauth-web\",\"customerId\":\"\",\"payload\":{\"event_category\":\"loginVerify\",\"screenName\":\"/loginVerify\",\"event_action\":\"login_successful\",\"event_label\":\"phone_number_otp\",\"utm_campaign\":\"Desktop\",\"utm_source\":\"paytm-unified-merchant-panel\",\"event\":\"custom_event\",\"vertical_name\":\"oauth\",\"referrer\":\"https://dashboard.paytm.com/\"},\"deviceId\":\"4b412630-2522-11ed-b80d-898c236db434\",\"messageVersion\":1,\"osType\":\"Win32\",\"osVersion\":null,\"brand\":\"Chrome\",\"model\":\"104\",\"ip\":null,\"userAgent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36\"}]";
        System.out.println(signalBody);
    }
}