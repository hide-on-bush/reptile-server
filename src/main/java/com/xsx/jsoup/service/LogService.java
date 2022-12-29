package com.xsx.jsoup.service;

import com.xsx.jsoup.common.util.HttpUtil;
import okhttp3.Response;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;

/**
 * @Author:夏世雄
 * @Date: 2022/07/19/14:51
 * @Version: 1.0
 * @E-mail: xiashixiongtoxx@163.com
 * @Discription:
 **/
@Service
public class LogService {

    public ChromeDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chromedriver.exe");
        ChromeOptions cap = new ChromeOptions();
        //cap.setExperimentalOption("w3c", false);
        cap.addArguments("no-sandbox");
        cap.addArguments("headless");
        LoggingPreferences logP = new LoggingPreferences();
        logP.enable(LogType.PERFORMANCE, Level.ALL);
        cap.setCapability(CapabilityType.LOGGING_PREFS, logP);
        return new ChromeDriver(cap);
    }

    public void getLogs() throws Exception {
        ChromeDriver driver = this.getDriver();
        driver.get("http://www.baidu.com");
        List<LogEntry> all = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
        //System.out.println(all);
        Response response = HttpUtil.sent("GET", "http://www.baidu.com", "application/json;charset=UTF-8", null, null);
        for (String value : response.headers().values("Set-Cookie")) {
            System.out.println("value =" + value);
        }
    }
}
