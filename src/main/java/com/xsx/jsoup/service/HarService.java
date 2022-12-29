package com.xsx.jsoup.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @Author:夏世雄
 * @Date: 2022/09/13/17:53
 * @Version: 1.0
 * @Discription:
 **/
@Service
public class HarService {


    public Map<String, String> har() throws Exception {
        Map<String, String> map = new HashMap<>();
        BrowserMobProxy proxy = getProxyServer(); //getting browsermob proxy
        System.out.println(proxy + "BrowserMobProxy");
        Proxy seleniumProxy = getSeleniumProxy(proxy);
        System.out.println(seleniumProxy + "seleniumProxy");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability("acceptInsecureCerts", true);
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/105.0.5195.52/chromedriver.exe");
        ChromeOptions options = new ChromeOptions().merge(capabilities);
        ChromeDriver driver = new ChromeDriver(options);
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        proxy.newHar("mysite");
        driver.get("https://netbanking.kotak.com/knb2/");
        WebElement userNameInput = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.id("userName")));
        userNameInput.sendKeys("K558665036");

        WebElement nextButton = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//*[@id=\"crnForm\"]/div[6]/button")));
        nextButton.click();
        Har har1 = proxy.getHar();
        HarLog log1 = har1.getLog();
        List<HarEntry> entries1 = log1.getEntries();
        entries1 = entries1.stream().filter(x -> x.getRequest().getUrl().equals("https://netbanking.kotak.com/knb2/login-service/v1/login")).collect(Collectors.toList());
        System.out.println("har1 =========" + JSON.toJSONString(entries1));

        Thread.sleep(5L);
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
        entries = entries.stream().filter(x -> x.getRequest().getUrl().equals("https://netbanking.kotak.com/knb2/login-service/v1/authenticate")).collect(Collectors.toList());
        System.out.println("har2 =========" + JSON.toJSONString(entries));
        if (!CollectionUtils.isEmpty(entries)) {
            HarEntry harEntry = entries.get(0);
            //请求https://netbanking.kotak.com/knb2/login-service/v1/authenticate的body，里面有state
            String requestBody = harEntry.getRequest().getPostData().getText();
            if (StringUtils.isNotBlank(requestBody)) {
                map.put("body", requestBody);
                JSONObject jsonObject = JSON.parseObject(requestBody);
                map.put("state", jsonObject.getString("state"));
            }
        }
        return map;
    }


    public Proxy getSeleniumProxy(BrowserMobProxy proxyServer) {
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxyServer);
        System.out.println(seleniumProxy.getHttpProxy());
        try {
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            System.out.println(hostIp);
            seleniumProxy.setHttpProxy(hostIp + ":" + proxyServer.getPort());
            seleniumProxy.setSslProxy(hostIp + ":" + proxyServer.getPort());
            System.out.println(proxyServer.getPort() + "is port number");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("invalid host");
        }
        return seleniumProxy;
    }

    public BrowserMobProxy getProxyServer() {
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.setTrustAllServers(true);
        proxy.start(0);
        return proxy;
    }


    public void har2() throws Exception {
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/105.0.5195.52/chromedriver.exe");
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start(0);

        // get the Selenium proxy object
        org.openqa.selenium.Proxy seleniumProxy =
                ClientUtil.createSeleniumProxy(proxy);
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.PROXY, seleniumProxy);
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--start-maximized");
        options.setCapability("acceptInsecureCerts", true);
        ChromeDriver driver = new ChromeDriver(options);
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
        entries1 = entries1.stream().filter(x -> x.getRequest().getUrl().equals("https://netbanking.kotak.com/knb2/login-service/v1/login")).collect(Collectors.toList());
        System.out.println("har2 =========" + JSON.toJSONString(entries1));
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
        entries = entries.stream().filter(x -> x.getRequest().getUrl().equals("https://netbanking.kotak.com/knb2/login-service/v1/authenticate")).collect(Collectors.toList());
        System.out.println("har2 =========" + JSON.toJSONString(entries));
    }
}
