package com.xsx.jsoup.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.openqa.selenium.*;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author:夏世雄
 * @Date: 2022/07/13/10:09
 * @Version: 1.0
 * @E-mail: xiashixiongtoxx@163.com
 * @Discription:
 **/
@Slf4j
@Service
public class BankCardService {

    private WebDriverService webDriverService;

    private AtomicInteger retries = new AtomicInteger(3);
    private AtomicInteger runTimes = new AtomicInteger(1);

    public BankCardService(WebDriverService webDriverService) {
        this.webDriverService = webDriverService;
    }


    public void login() {
        ChromeDriver chromeDriver = null;
        try {
            String loginUrl =  "https://my.idfcfirstbank.com/login";
            chromeDriver = webDriverService.getChromeDriver(loginUrl);
            new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.id("app")));
            this.inputMobileAndSubmit(chromeDriver);
            this.inputPwdAndSubmit(chromeDriver);
        } catch (Exception e) {
            log.error("执行异常， error= {}", e.getLocalizedMessage());
            if (chromeDriver != null) {
                chromeDriver.quit();
            }
            log.info("执行次数 ={}", runTimes);
            runTimes.incrementAndGet();
            if(retries.intValue() > 0){
                retries.decrementAndGet();
                login();
            }
        }

    }

    /**
     * 输入手机号并提交
     * @param chromeDriver
     */
    private void inputMobileAndSubmit(ChromeDriver chromeDriver) {
        WebElement mobileInput = new WebDriverWait(chromeDriver, Duration.ofSeconds(30L)).until(ExpectedConditions
                .presenceOfElementLocated(By.name("mobileNumber")));
        mobileInput.sendKeys("8197132475");
        WebElement submitButton = chromeDriver.findElement(By.className("ksvYSp"));
        submitButton.submit();
    }

    /**
     * 输入密码并提交
     * @param chromeDriver
     */
    private void inputPwdAndSubmit(ChromeDriver chromeDriver) {
        //显示等待元素加载完毕，如果在设置的时间内还没有出现，则抛异常
        WebElement passwordInput = new WebDriverWait(chromeDriver, Duration.ofSeconds(30L)).until(ExpectedConditions
                .presenceOfElementLocated(By.name("login-password-input")));
        passwordInput.sendKeys("Raju@0901");
        WebElement loginButton = new WebDriverWait(chromeDriver, Duration.ofSeconds(30L)).until(ExpectedConditions
                .presenceOfElementLocated(By.className("ksvYSp")));
        loginButton.submit();
        //获取当前页面的所有内容
        WebElement indexPage = new WebDriverWait(chromeDriver, Duration.ofSeconds(30L)).until(ExpectedConditions
                .presenceOfElementLocated(By.cssSelector("body")));
        //隐式等待 优点：隐性等待对整个driver的周期都起作用，所以只要设置一次即可。
        //缺点：使用隐式等待，程序会一直等待整个页面加载完成，才会执行下一步操作；
        chromeDriver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
        chromeDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        String pageSource = chromeDriver.getPageSource();
        this.printCookies(chromeDriver);
        //TODO 打印所有的headers（request header 和response header）
        this.printHeaders(chromeDriver);
    }

    /**
     * 打印cookies
     * @param chromeDriver
     */
    private void printCookies(ChromeDriver chromeDriver) {
        Set<Cookie> cookies = chromeDriver.manage().getCookies();
        cookies.forEach(x -> System.out.println(x));
    }

    /**
     * 打印headers
     * @param chromeDriver
     */
    private void printHeaders(ChromeDriver chromeDriver){
        String currentUrl = chromeDriver.getCurrentUrl();
        log.info("currentUrl ={}", currentUrl);
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建Request实例，可以配置接口地址和请求头
        Request okRequest = new Request.Builder().url(currentUrl).build();
        Headers requestHeaders = okRequest.headers();
        log.info("requestHeaders = {}", requestHeaders);
        //GET请求，用Response接受相应结果
        try {
            Response response = okHttpClient.newCall(okRequest).execute();
            Headers responseHeaders =  response.headers();
            log.info("responseHeaders = {}", responseHeaders);
        } catch (Exception e) {
            log.error("获取headers  error = {}", e.getLocalizedMessage());
        }

    }



}
