package com.xsx.jsoup.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;


@Service
@Slf4j
public class WebDriverService {

    public void useWebDriver() throws Exception{
        ChromeDriver driver = getChromeDriver("https://www.xiaohongshu.com/");
        log.info("单击事件");
        this.clickDemo(driver);
        //WebElement webElement = driver.findElement(By.cssSelector("#app > div > div.header.red-header.dark-theme > ul > a:nth-child(5)"));
//        Actions action = new Actions(driver);
//        action.click(webElement);
        //webElement.click();
//        //quit方法是关闭浏览器
//        //driver.quit();

    }


    public ChromeDriver getChromeDriver(String url) {
        //这一步必不可少
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chormeDriver/chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get(url);
        return driver;
    }

    public void clickDemo(ChromeDriver driver) throws InterruptedException {
        WebElement news = new WebDriverWait(driver, Duration.ofSeconds(5L)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("新闻中心")));
        //element.click();
        driver.executeScript("arguments[0].click()", news);
        Thread.sleep(5000L);
        WebElement aboutUs = driver.findElement(By.linkText("关于我们"));
        driver.executeScript("arguments[0].click()", aboutUs);

        Set<Cookie> cookies = driver.manage().getCookies();
        cookies.forEach(x-> System.out.println(x));

    }


    public void getHeaders() throws Exception{
        //1.使用webDriver打开小红书
        String url = "https://www.xiaohongshu.com/";
        ChromeDriver driver = getChromeDriver(url);

        log.info("Cookie===============================");
        Set<Cookie> cookies = driver.manage().getCookies();
        cookies.forEach(x-> System.out.println(x));
        
        //2.使用jsoup解析页面，获取链接
        String pageSource = driver.getPageSource();
        Document document = Jsoup.parse(pageSource);
        Elements news = document.select("#app > div > div.header.red-header.dark-theme.home-header > ul > a:nth-child(5)");
        String href = news.attr("href");

        //3.使用OkHttpClient调用
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建Request实例，可以配置接口地址和请求头
        Request okRequest = new Request.Builder().url(url + href).build();
        Headers requestHeader = okRequest.headers();
        log.info("requestHeader===============================");
        System.out.println(requestHeader);
        //GET请求，用Response接受相应结果
        Response response = okHttpClient.newCall(okRequest).execute();

        Headers responseHeader = response.headers();
        log.info("responseHeader===============================");
        System.out.println(responseHeader);
        System.out.println(response.body().string());

    }

}
