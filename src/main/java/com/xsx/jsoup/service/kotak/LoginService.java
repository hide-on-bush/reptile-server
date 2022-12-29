package com.xsx.jsoup.service.kotak;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xsx.jsoup.common.util.HttpUtil;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.proxy.CaptureType;
import okhttp3.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @Author:夏世雄
 * @Date: 2022/09/14/10:33
 * @Version: 1.0
 * @Discription:
 **/
@Service
public class LoginService {


    public Map<String, String> login() throws Exception {
        BrowserProxy browserProxy = BrowserProxy.getInstance();
        WebDriver driver = browserProxy.getDriver();
        BrowserMobProxy proxy = browserProxy.getProxy();
        return doLogin(driver, proxy);
    }

    public Map<String, String> doLogin(WebDriver driver, BrowserMobProxy proxy) throws Exception {
        Map<String, String> map = new HashMap<>();
        proxy.newHar("test_har");
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
        HarLog log1 = har1.getLog();
        List<HarEntry> entries1 = log1.getEntries();
        entries1 = entries1.stream().filter(x -> x.getRequest().getUrl().equals("https://netbanking.kotak.com/knb2/login-service/v1/login")).collect(Collectors.toList());
        System.out.println("har1 =========" + JSON.toJSONString(entries1));
        if (!CollectionUtils.isEmpty(entries1)) {
            HarEntry harEntry = entries1.get(0);
            JSONObject jsonObject = JSON.parseObject(harEntry.getResponse().getContent().getText());
            String state = jsonObject.getString("state");
            map.put("state", state);
        }
        pwdInput.sendKeys("123456@Riya");
        WebElement loginButton = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.className("btn-primary")));
        loginButton.click();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(50L));
        Har har = proxy.getHar();
        HarLog log = har.getLog();
        List<HarEntry> entries = log.getEntries();
        entries = entries.stream().filter(x -> x.getRequest().getUrl().equals("https://netbanking.kotak.com/knb2/login-service/v1/authenticate")).collect(Collectors.toList());
        System.out.println("har2 =========" + JSON.toJSONString(entries));
        if (!CollectionUtils.isEmpty(entries)) {
            HarEntry harEntry = entries.get(0);
            String authBody = harEntry.getRequest().getPostData().getText();
            map.put("authBody", authBody);
        }
        return map;
    }


    public Map<String, String> test1() throws Exception {
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start(0);
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/105.0.5195.52/chromedriver.exe");
        // get the Selenium proxy object
        //Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy, InetAddress.getLocalHost());

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        SocketAddress sa = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 4780);

        seleniumProxy.setHttpProxy(sa.toString());
//        seleniumProxy.setSslProxy(sa.toString());
        // configure it as a desired capability
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--proxy-server=http://localhost：4780");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability("acceptInsecureCerts", true);

        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        // start the browser up
        //options.merge(capabilities);
        WebDriver driver = new ChromeDriver(capabilities);
        // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

        // create a new HAR with the label "yahoo.com"
        proxy.newHar("yahoo.com");

        // open yahoo.com
        //driver.get("http://yahoo.com");

        // get the HAR data
        //Har har = proxy.getHar();
        return doLogin(driver, proxy);
    }

    public void getToken() throws Exception {
        Map<String, String> map = test1();
        if (map.containsKey("authBody")) {
            System.out.println(getToken(map.get("authBody")));
        }
    }


    public String getToken(String body) throws Exception {
        String url = "https://netbanking.kotak.com/knb2/login-service/v1/authenticate";
        Map<String, String> authenticateHeader = buildAuthenticateHeader();
        Response response = HttpUtil.sent("POST", url, "application/json", body, authenticateHeader, false);
        if (response.code() == 200) {
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            return jsonObject.getString("accessToken");
        }
        return null;
    }

    public static final String USERAGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36";

    public Map<String, String> buildAuthenticateHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json, text/plain, */*");
        map.put("Accept-Language", "zh-CN,zh;q=0.9");
        map.put("Connection", "keep-alive");
        map.put("Content-Length", "1894");
        map.put("Content-Type", "application/json");
        map.put("Cookie", "");
        map.put("Host", "netbanking.kotak.com");
        map.put("nonce", "0111344724601928871430071900259002235060566|056861244804252976690258260530102607858302");
        map.put("Origin", "https://netbanking.kotak.com");
        map.put("Referer", "https://netbanking.kotak.com/knb2/");
        map.put("sec-ch-ua", "\"Chromium\";v=\"104\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"104\"");
        map.put("sec-ch-ua-mobile", "?0");
        map.put("sec-ch-ua-platform", "\"Windows\"");
        map.put("Sec-Fetch-Dest", "empty");
        map.put("Sec-Fetch-Mode", "cors");
        map.put("Sec-Fetch-Site", "same-origin");
        map.put("User-Agent", USERAGENT);
        return map;
    }

}
