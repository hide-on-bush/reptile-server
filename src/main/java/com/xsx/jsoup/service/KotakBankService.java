package com.xsx.jsoup.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xsx.jsoup.common.util.SslUtil;
import com.xsx.jsoup.service.Kotak.ChromeDriverProxy;
import com.xsx.jsoup.service.Kotak.ResponseReceivedEvent;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;


import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @Author:夏世雄
 * @Date: 2022/09/09/13:13
 * @Version: 1.0
 * @Discription:
 **/
@Slf4j
@Service
public class KotakBankService {

    public static final String NETWORK_RESPONSE_RECEIVED = "Network.responseReceived";

    public void login() throws Exception{
        String url = "https://netbanking.kotak.com/knb2/";

        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/105.0.5195.52/chromedriver.exe");
        ChromeOptions cap = new ChromeOptions();
        //禁用w3c
        cap.setExperimentalOption("w3c", false);
        //禁用沙盒
        cap.addArguments("no-sandbox");
        //无界面启动
        //cap.addArguments("headless");
        LoggingPreferences logP = new LoggingPreferences();
        logP.enable(LogType.PERFORMANCE, Level.ALL);
        cap.setCapability(CapabilityType.LOGGING_PREFS, logP);
        ChromeDriverProxy  chromeDriver = new ChromeDriverProxy(cap);
        chromeDriver.get(url);

        //第一步获取userName输入框，输入用户，并点击next按钮，获取响应的state
        WebElement userNameInput = new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.id("userName")));
        userNameInput.sendKeys("K558665036");

        WebElement nextButton = new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//*[@id=\"crnForm\"]/div[6]/button")));
        nextButton.click();
        LogEntries logs0 = chromeDriver.manage().logs().get("performance");
        //getRequestHeaders(logs0);
        //第二步获取password输入框，输入密码，并点击login按钮，获取响应的 accessToken
        WebElement pwdInput = new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.id("credentialInputField")));
        pwdInput.sendKeys("123456@Riya");


        WebElement loginButton = new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.className("btn-primary")));
        loginButton.click();
        new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.className("body-dashboard")));

//        Thread.sleep(5L);
//        String windowHandle = chromeDriver.getWindowHandle();
//        chromeDriver.switchTo().window(windowHandle);
//        String pageSource = chromeDriver.getPageSource();
//        System.out.println(pageSource);
        Logs logs = chromeDriver.manage().logs();
        Set<String> availableLogTypes = logs.getAvailableLogTypes();
        //System.out.println(logs.get("performance"));
        if(availableLogTypes.contains(LogType.PERFORMANCE)) {
            LogEntries logEntries = logs.get(LogType.PERFORMANCE);
            List<ResponseReceivedEvent> responseReceivedEvents = new ArrayList<>();

            for(LogEntry entry : logEntries) {
                JSONObject jsonObj = JSON.parseObject(entry.getMessage()).getJSONObject("message");
                String method = jsonObj.getString("method");
                String params = jsonObj.getString("params");

                if (method.equals(NETWORK_RESPONSE_RECEIVED)) {
                    ResponseReceivedEvent response = JSON.parseObject(params, ResponseReceivedEvent.class);
                    responseReceivedEvents.add(response);
                }
            }
            doSaveHttpTransferDataIfNecessary(chromeDriver, responseReceivedEvents);
        }




        //LogEntries logs = chromeDriver.manage().logs().get("performance");
//        for (LogEntry logEntry : logs) {
//            System.out.println(logEntry);
//        }
       // getRequestHeaders(logs);
    }

    private  void doSaveHttpTransferDataIfNecessary(ChromeDriverProxy driver, List<ResponseReceivedEvent> responses) {
        for(ResponseReceivedEvent responseReceivedEvent : responses) {
            String url = JSONObject.parseObject(responseReceivedEvent.getResponse()).getString("url");
            boolean staticFiles = url.endsWith(".png")
                    || url.endsWith(".jpg")
                    || url.endsWith(".css")
                    || url.endsWith(".ico")
                    || url.endsWith(".js")
                    || url.endsWith(".gif");

            if(!staticFiles && url.startsWith("https")) {
                if (url.equals("https://netbanking.kotak.com/knb2/login-service/v1/login")) {
                    String  bodyx= driver.getResponseBody(responseReceivedEvent.getRequestId());
                    JSONObject jsonObject = JSON.parseObject(bodyx);
                    String state = jsonObject.getString("state");
                    System.out.println("state============" + state);
                }
                if (url.equals("https://netbanking.kotak.com/knb2/login-service/v1/authenticate")) {
                    String  bodyx= driver.getResponseBody(responseReceivedEvent.getRequestId());
                    JSONObject jsonObject = JSON.parseObject(bodyx);
                    String accessToken = jsonObject.getString("accessToken");
                    System.out.println("accessToken============" + accessToken);
                }
                // 使用上面开发的接口获取返回数据
//                String  bodyx= driver.getResponseBody(responseReceivedEvent.getRequestId());
//
//                System.out.println("url:"+url+" ,body-->"+bodyx);
//                JSONArray cookies = driver.getCookies(responseReceivedEvent.getRequestId());
//                System.out.println("url:"+url+" ,cookies-->"+cookies);
            }
        }
    }


    private Set<JSONObject> getRequestHeaders(LogEntries logs) {
        Set<JSONObject> requestHeader = new HashSet<>();
        for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();) {
            LogEntry entry = it.next();
            JSONObject json = JSONObject.parseObject(entry.getMessage());
            JSONObject message = json.getJSONObject("message");
            String method = message.getString("method");
            //获取请求头
            if (StringUtils.isNotBlank(method) && "Network.responseReceived".equals(method)) {
                JSONObject params = message.getJSONObject("params");
                JSONObject response = params.getJSONObject("response");
                String messageUrl = response.getString("url");
                if ("https://netbanking.kotak.com/knb2/login-service/v1/login".equals(messageUrl)) {
                    //String body = chromeDevTools.send(Network.getResponseBody(new RequestId(""))).getBody();
                    //获取state
                    System.out.println("response===================" + response);
                }
                if ("https://netbanking.kotak.com/knb2/login-service/v1/authenticate".equals(messageUrl)) {
                    //System.out.println("header============" + response.get("headers"));
                    requestHeader.add((JSONObject)response.get("headers"));
                    JSONArray data = (JSONArray)response.get("postDataEntries");
                    JSONObject tokenObject = (JSONObject)data.get(0);
                    String token = (String)tokenObject.get("bytes");
                    System.out.println("token========" + token);
                }
                System.out.println(params);
                System.out.println(response);
                System.out.println(messageUrl );
            }
        }
        return requestHeader;
    }



    public void get1() throws Exception{
        SslUtil.ignoreSsl();
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start(0);

        Proxy selProxy = ClientUtil.createSeleniumProxy(proxy);
        selProxy.setHttpProxy("localhost:4780")
               .setSslProxy("localhost:4780")
               .setFtpProxy("localhost:4780");
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/105.0.5195.52/chromedriver.exe");
        ChromeOptions cap = new ChromeOptions();
        //禁用w3c
        cap.setExperimentalOption("w3c", false);
        cap.setCapability(CapabilityType.PROXY, selProxy);
        //禁用沙盒
        cap.addArguments("no-sandbox");
        //无界面启动
        //cap.addArguments("headless");
        LoggingPreferences logP = new LoggingPreferences();
        logP.enable(LogType.PERFORMANCE, Level.ALL);
        cap.setCapability(CapabilityType.LOGGING_PREFS, logP);
        cap.setCapability("acceptInsecureCerts", true);
        ChromeDriver driver = new ChromeDriver(cap);



        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

        proxy.newHar("mysite");

        driver.get("https://netbanking.kotak.com/knb2/");

        WebElement userNameInput = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.id("userName")));
        userNameInput.sendKeys("K558665036");

        WebElement nextButton = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//*[@id=\"crnForm\"]/div[6]/button")));
        nextButton.click();
        Thread.sleep(5L);
        Har har1 = proxy.getHar();
        HarLog log1 = har1.getLog();
        List<HarEntry> entries1 = log1.getEntries();
        System.out.println("har1 =========" + JSON.toJSONString(entries1));

        WebElement pwdInput = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.id("credentialInputField")));
        pwdInput.sendKeys("123456@Riya");
        WebElement loginButton = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.className("btn-primary")));
        loginButton.click();
        Thread.sleep(5L);

        Har har = proxy.getHar();
        HarLog log = har.getLog();
        List<HarEntry> entries = log.getEntries();
        System.out.println("har2 =========" + JSON.toJSONString(entries));
    }


   public void proxy() throws Exception{

       //String hostAndPort = "localhost:4780";
       int port = 15172;
       //String host = "112.91.159.66";

       System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/105.0.5195.52/chromedriver.exe");
       //start the proxy
       BrowserMobProxy proxy = new BrowserMobProxyServer();
//       Map<String, String> headers = new HashMap<String, String>();
//       proxy.addHeaders(headers);
       proxy.start(port);


       Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
//       seleniumProxy.setHttpProxy(hostAndPort)
//               .setSslProxy(hostAndPort)
//               .setFtpProxy(hostAndPort);

       DesiredCapabilities capabilities = new DesiredCapabilities();
       LoggingPreferences logP = new LoggingPreferences();
       logP.enable(LogType.PERFORMANCE, Level.ALL);
       capabilities.setCapability(CapabilityType.LOGGING_PREFS, logP);
       capabilities.setCapability("acceptInsecureCerts", true);
       capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
       ChromeOptions option = new ChromeOptions().merge(capabilities);

       WebDriver driver = new ChromeDriver(option);
       proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
       proxy.newHar("mysite");
       Thread.sleep(3000);
       driver.get("https://netbanking.kotak.com/knb2/");

       WebElement userNameInput = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
               .presenceOfElementLocated(By.id("userName")));
       userNameInput.sendKeys("K558665036");

       WebElement nextButton = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
               .presenceOfElementLocated(By.xpath("//*[@id=\"crnForm\"]/div[6]/button")));
       nextButton.click();


       WebElement pwdInput = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
               .presenceOfElementLocated(By.id("credentialInputField")));
       Thread.sleep(5L);
       Har har1 = proxy.getHar();
       System.out.println("har1 ===="+ JSON.toJSON(har1.getLog()));
       pwdInput.sendKeys("123456@Riya");
       WebElement loginButton = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
               .presenceOfElementLocated(By.className("btn-primary")));
       loginButton.click();
       driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(50L));
       Har har = proxy.getHar();
       System.out.println(JSON.toJSON(har));
       HarLog log = har.getLog();
       System.out.println("har2 =========" + JSON.toJSON(log));
   }
}
