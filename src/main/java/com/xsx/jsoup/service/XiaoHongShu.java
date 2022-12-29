package com.xsx.jsoup.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSink;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @Author:夏世雄
 * @Date: 2022/07/20/12:13
 * @Version: 1.0
 * @E-mail: xiashixiongtoxx@163.com
 * @Discription:
 **/
@Service
@Slf4j
public class XiaoHongShu {

    public void test() throws Exception {
        Map<String, Map<String, String>> map = this.login();
        Map<String, String> headers = map.get("requestHeader");
        Response response = this.send(headers, "https://www.processon.com/folder/loadfiles");
        System.out.println(response.body());
    }

    public Map<String, Map<String, String>> login() {
        Map<String, Map<String, String>> map = new HashMap<>();
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //无界面参数
        //options.addArguments("headless");
        //禁用沙盒
        options.addArguments("no-sandbox");
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setBrowserName("chrome");
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        // set performance logger
        // this sends Network.enable to chromedriver
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        cap.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        ChromeDriver chromeDriver = new ChromeDriver(cap);
        chromeDriver.navigate().to("https://www.processon.com/");
        WebElement button_login_btn = new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.className("login_btn")));
        button_login_btn.click();
        //link-to-account
        chromeDriver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
//        new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
//                .presenceOfElementLocated(By.id()));
        chromeDriver.switchTo().frame("loginWindow-content");
        WebElement accountElement = chromeDriver.findElement(By.cssSelector("#other-logins > ol > li.link-to-account > span"));
        accountElement.click();

        WebElement nameInput = new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.name("name")));
        nameInput.sendKeys("15068170059");
        WebElement pwdInput = new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.name("pwd")));
        pwdInput.sendKeys("xsx123456");
        WebElement submitBut = new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.id("submit-pwd")));
        submitBut.click();
        chromeDriver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
        List<LogEntry> logs = chromeDriver.manage().logs().get(LogType.PERFORMANCE).getAll();
        Set<JSONObject> requestHeader = getRequestHeaders(logs);
        map.put("requestHeader", parseMap(JSON.toJSONString(requestHeader)));
        System.out.println(logs);
        return map;
    }

    public Map<String, String> parseMap(String source) {
        Map<String, String> result = new HashMap<>();
        if (StringUtils.isNotBlank(source)) {
            if (source.trim().startsWith("[")) {
                JSONArray array = JSONArray.parseArray(source);
                List<JSONObject> jsonObjectList = array.toJavaList(JSONObject.class);
                if (!CollectionUtils.isEmpty(jsonObjectList)) {
                    for (JSONObject item : jsonObjectList) {
                        result.putAll((Map) JSON.parse(item.toString()));
                    }
                }
            } else {
                result = (Map) JSON.parse(source);
            }
        }
        return result;
    }


    public Set<JSONObject> getRequestHeaders(List<LogEntry> logs) {
        Set<JSONObject> requestHeader = new HashSet<>();
        if (!CollectionUtils.isEmpty(logs)) {
            for (LogEntry entry : logs) {
                JSONObject json = JSONObject.parseObject(entry.getMessage());
                JSONObject message = json.getJSONObject("message");
                String method = message.getString("method");
                //获取请求头
                if (method != null && "Network.requestWillBeSent".equals(method)) {
                    JSONObject params = message.getJSONObject("params");
                    JSONObject request = params.getJSONObject("request");
                    String messageUrl = request.getString("url");
                    if ("https://www.processon.com/".equals(messageUrl)) {
                        requestHeader.add((JSONObject) request.get("headers"));
                    }
                }
            }
        }
        return requestHeader;
    }


    public Response send(Map<String, String> headers, String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();

//        Request request = new Request.Builder()
//                .addHeader("content-type", "application/json")
//                .url("url")
//                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),""))
//                .build();


        //"https://app.my.idfcfirstbank.com/api/payments/v1/accounts"
        HashMap<String, String> paramMap = new HashMap<>();
        Request.Builder builder = new Request.Builder().url(url);
        builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramMap.toString()));
        builder.addHeader("content-type", "application/json");
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach((k, v) -> {
                log.info("k={},v={}", k, v);
                builder.addHeader(k, v);
            });
        }

        Request request = builder.build();

        Response response = client.newCall(request).execute();
        //ResponseBody body = response.body();
        //System.out.println("body =====" + body.string());
        log.info("request ={}", request.headers());
        return response;
    }

}
