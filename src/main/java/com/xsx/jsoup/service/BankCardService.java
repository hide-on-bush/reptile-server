package com.xsx.jsoup.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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


    public BankCardService() {

    }


    public void login() {
        int reties = 3;
        int runTimes = 1;
        String loginUrl = "https://my.idfcfirstbank.com/login";
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/chromedriver.exe");
        ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.get(loginUrl);
        while (reties >= 0) {
            try {
                log.info("执行次数 ={}", runTimes);
                doLogin(chromeDriver);
                return;
            } catch (Exception e) {
                log.error("执行异常， error= {}", e);
                reties--;
                runTimes++;
            }
        }
    }

    public void doLogin(ChromeDriver chromeDriver) {
        new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.id("app")));
        this.inputMobileAndSubmit(chromeDriver);
        this.inputPwdAndSubmit(chromeDriver);
    }

    public void inputPwdAndSubmit(ChromeDriver chromeDriver) {
        WebElement passwordInput = new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.name("login-password-input")));
        passwordInput.sendKeys("Raju@0901");
        WebElement loginButton = new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.className("ksvYSp")));
        loginButton.submit();
        new WebDriverWait(chromeDriver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.className("active")));
        chromeDriver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);

    }


    /**
     * 输入手机号并提交
     *
     * @param chromeDriver
     */
    public void inputMobileAndSubmit(ChromeDriver chromeDriver) {
        WebElement mobileInput = new WebDriverWait(chromeDriver, Duration.ofSeconds(100L)).until(ExpectedConditions
                .presenceOfElementLocated(By.name("mobileNumber")));
        mobileInput.sendKeys("8197132475");
        WebElement submitButton = chromeDriver.findElement(By.className("ksvYSp"));
        submitButton.submit();
    }


    /**
     * 打印cookies
     *
     * @param chromeDriver
     */
    private void printCookies(ChromeDriver chromeDriver) {
        Set<Cookie> cookies = chromeDriver.manage().getCookies();
        cookies.forEach(x -> System.out.println(x));
    }


}
