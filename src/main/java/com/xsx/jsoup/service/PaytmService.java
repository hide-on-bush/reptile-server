package com.xsx.jsoup.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xsx.jsoup.common.util.HttpUtil;
import com.xsx.jsoup.entity.BankUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author:夏世雄
 * @Date: 2022/08/26/14:36
 * @Version: 1.0
 * @Discription:
 **/
@Service
@Slf4j
public class PaytmService {

    @Autowired
    StringRedisTemplate redisTemplate;

    public void login(BankUser bankUser) throws Exception{
        String loginUrl = "https://dashboard.paytm.com/login/ ";
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/chromedriver.exe");
        ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.get(loginUrl);
        chromeDriver.manage().timeouts().pageLoadTimeout(30L, TimeUnit.SECONDS);
        Set<Cookie> cookies = chromeDriver.manage().getCookies();
        StringBuilder cookie = new StringBuilder();
        for (Cookie item : cookies) {
            if (item.toString().contains("signalSDKVisitorId") || item.toString().contains("XSRF-TOKEN")
                || item.toString().contains("SESSION")) {
                String[] array = item.toString().split(";");
                cookie.append(array[0]).append(";");
            }
        }
        System.out.println(cookie);
        String authState = step1Request(cookie.toString());
        if (StringUtils.isBlank(authState)) {
            return;
        }
        Map<String, String>  step2ResultMap = step2Request(authState, bankUser, cookie.toString());
        System.out.println("step2ResultMap=======" + step2ResultMap);
        if (CollectionUtils.isEmpty(step2ResultMap) || "FAILURE".equals(step2ResultMap.get("status"))) {
            return;
        }
        if (!step2ResultMap.containsKey("redirectUrl")) {
            //说明没有登录过，则要接收otp，并校验otp
            step3Request(cookie, authState, step2ResultMap);
        } else {
            //说明登录过且已经登录成功,将cookie放入redis中
            //访问GET https://dashboard.paytm.com/next/ 是否成功，校验登录是否成功
        }

    }

    public void step3Request(StringBuilder cookie, String authState, Map<String, String> step2ResultMap) throws IOException {
        String otp = redisTemplate.opsForValue().get("otp");
        String step3Url = "https://accounts.paytm.com/login/validate/otp";
        Map<String, String> step3Header = sendOtpHeader(cookie.toString());
        String  step3Body = "{\"otp\":\""+ otp+ "\",\"state\":\""+ step2ResultMap.get("stateCode") +"\",\"csrfToken\":\""+ authState+ "\"}";
        System.out.println("step3ReqBody == " + step3Body);
        Response response3 = HttpUtil.sent("POST", step3Url, "application/json", step3Body, step3Header);
        System.out.println("step3Response.code = " + response3.code());
        System.out.println("step3Response.body= " + response3.body().string());
        if (response3.code() == 200) {

        }
    }


    public String step1Request(String cookie) throws Exception{
        String authState = "";
        String step1Url = "https://accounts.paytm.com/um/authorize/init";
        Map<String, String> step1Header = step1Header(cookie);
        String body = "{\"clientId\":\"paytm-unified-merchant-panel\",\"responseType\":\"code\",\"scope\":\"paytm\",\"redirectUri\":\"https://dashboard.paytm.com/auth\"}";
        Response response = HttpUtil.sent("POST", step1Url, "application/json", body, step1Header);
        System.out.println("step1.code =" + response.code());
        if (response.code() == 200) {
            String responseBody = response.body().string();
            System.out.println("第一步responseBody = " + responseBody);
            JSONObject jsonObject = JSON.parseObject(responseBody);
            System.out.println("step1.response.body =" + jsonObject);
            JSONObject data = (JSONObject)jsonObject.get("data");
            authState = data.getString("authState");
        }
        System.out.println("authState=" + authState);
        return authState;
    }

    public static final String  USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36";

    public Map<String, String> step1Header(String cookie) {
        Map<String, String> map = new HashMap<>();
        map.put("accept", "*/*");
        map.put("accept-language", "zh-CN,zh;q=0.9");
        map.put("content-length", "130");
        map.put("content-type", "application/json");
        map.put("cookie", cookie);
        map.put("origin", "https://accounts.paytm.com");
        map.put("referer", "https://accounts.paytm.com/oauth-js-sdk/index.html");
        map.put("sec-ch-ua", "\"Chromium\";v=\"104\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"104\"");
        map.put(":authority", "accounts.paytm.com");
        map.put(":method", "POST");
        map.put(":path", "/um/authorize/init");
        map.put(":scheme", "https");
        map.put("sec-fetch-dest", "empty");
        map.put("sec-fetch-mode", "cors");
        map.put("sec-fetch-site", "same-origin");
        map.put("sec-ch-ua-mobile", "?0");
        map.put("user-agent", USER_AGENT);
        map.put("sec-ch-ua-platform", "\"Windows\"");
        return map;
    }


    public Map<String, String> step2Request(String authState, BankUser bankUser, String cookie) throws Exception{
        Map<String, String> result = new HashMap<>();
        String step2Url = "https://accounts.paytm.com/um/authorize/proceed";
        Map<String, String> step2Header = step2Header(cookie);
        //{"userName":"9059860438","password":"Wp123456@!","clientId":"paytm-unified-merchant-panel","csrfToken":"aa623c8a-bf4d-51e6-afed-5538633e41b8"}
        String body = "{\"userName\":\"" +bankUser.getLoginName() + "\",\"password\":\"" + bankUser.getPassword() + "\",\"clientId\":\"paytm-unified-merchant-panel\",\"csrfToken\":\"" +authState + "\"}";
        System.out.println("step2RequestBody" + body);
        Response response = HttpUtil.sent("POST", step2Url, "application/json", body, step2Header);
        System.out.println("step2Response.code = " + response.code());
        if (response.code() == 200) {
            String responseBody = response.body().string();
            System.out.println("第二步responseBody =" + responseBody);
            JSONObject jsonObject = JSON.parseObject(responseBody);
            System.out.println("response2.body = " + jsonObject);
            String redirectUrl = jsonObject.getString("redirectUrl");
            if (StringUtils.isNotBlank(redirectUrl)) {
                result.put("redirectUrl", redirectUrl);
            }
            String stateCode = jsonObject.getString("stateCode");
            result.put("stateCode", stateCode);
            String status = jsonObject.getString("status");
            result.put("status", status);
        }
        return result;
    }

    public Map<String, String> step2Header(String cookie) {
        Map<String, String> map = new HashMap<>();
        map.put("accept", "*/*");
        map.put("accept-language", "zh-CN,zh;q=0.9");
        map.put("content-length", "142");
        map.put("content-type", "application/json");
        map.put("cookie", cookie);
        map.put("origin", "https://accounts.paytm.com");
        map.put("referer", "https://accounts.paytm.com/oauth-js-sdk/index.html");
        map.put("sec-ch-ua", "\"Chromium\";v=\"104\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"104\"");
        map.put(":authority", "accounts.paytm.com");
        map.put(":method", "POST");
        map.put(":path", "/um/authorize/proceed");
        map.put(":scheme", "https");
        map.put("sec-fetch-dest", "empty");
        map.put("sec-fetch-mode", "cors");
        map.put("sec-fetch-site", "same-origin");
        map.put("sec-ch-ua-mobile", "?0");
        map.put("user-agent", USER_AGENT);
        map.put("sec-ch-ua-platform", "\"Windows\"");
        return map;
    }

    public Map<String, String> sendOtpHeader(String cookie) {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "*/*");
        map.put("Accept-Language", "zh-CN,zh;q=0.9");
        map.put("Connection", "keep-alive");
        map.put("Content-Length", "114");
        map.put("Content-Type", "application/json");
        map.put("Cookie", cookie);
        map.put("Host", "accounts.paytm.com");
        map.put("Origin", "https://accounts.paytm.com");
        map.put("Referer", "https://accounts.paytm.com/oauth-js-sdk/index.html");
        map.put("Sec-fetch-dest", "empty");
        map.put("Sec-fetch-mode", "cors");
        map.put("Sec-fetch-site", "same-origin");
        map.put("User-Agent", USER_AGENT);
        map.put("sec-ch-ua-mobile", "?0");
        map.put("sec-ch-ua", "\"Chromium\";v=\"104\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"104\"");
        map.put("sec-ch-ua-platform", "\"Windows\"");
        return map;
    }





}
