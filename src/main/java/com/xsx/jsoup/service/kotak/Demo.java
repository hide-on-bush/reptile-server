package com.xsx.jsoup.service.kotak;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @Author:夏世雄
 * @Date: 2022/09/14/12:11
 * @Version: 1.0
 * @Discription:
 **/

public class Demo {

    public static void main(String[] args) {
        new Demo().test("");
    }


    public void test(String url) {
        url = "https://netbanking.kotak.com/knb2/";
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/105.0.5195.52/chromedriver.exe");
        ChromeDriverProxy driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("w3c", false);
            options.addArguments("--disable-popup-blocking"); // 禁用阻止弹出窗口
            options.addArguments("no-sandbox"); // 启动无沙盒模式运行
            options.addArguments("disable-extensions"); // 禁用扩展
            options.addArguments("no-default-browser-check"); // 默认浏览器检查
            Map<String, Object> prefs = new HashMap();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefs);// 禁用保存密码提示框
            options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            options.setCapability("acceptInsecureCerts", true);
            // set performance logger
            // this sends Network.enable to chromedriver
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

            options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

            driver = new ChromeDriverProxy(options);
            // do something
            //"https://netbanking.kotak.com/knb2/"
            doSomething(url, driver);

            ChromeDriverProxy.saveHttpTransferDataIfNecessary(driver);
        } finally {
            //driver.close();
        }
    }

    public void doSomething(String url, ChromeDriverProxy driver) {

        driver.get(url);
        WebElement userNameInput = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.id("userName")));
        userNameInput.sendKeys("K558665036");

        WebElement nextButton = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("//*[@id=\"crnForm\"]/div[6]/button")));
        nextButton.click();

        WebElement pwdInput = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.id("credentialInputField")));

        pwdInput.sendKeys("123456@Riya");
        WebElement loginButton = new WebDriverWait(driver, Duration.ofSeconds(50L)).until(ExpectedConditions
                .presenceOfElementLocated(By.className("btn-primary")));
        loginButton.click();

//        new WebDriverWait(driver, Duration.ofSeconds(10L)).until(ExpectedConditions
//                .presenceOfElementLocated(By.className("icon-Home")));
//
//       // driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(50L));

    }

}
