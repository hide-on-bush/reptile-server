package com.xsx.jsoup.service;

import com.xsx.jsoup.common.util.CopyOfCleanLines;
import com.xsx.jsoup.common.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @Author:夏世雄
 * @Date: 2022/07/28/20:14
 * @Version: 1.0
 * @Discription:
 **/
@Service
@Slf4j
public class FednetBankService {


    public ChromeDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "D:/xsx-tools/chromedriver.exe");
        ChromeOptions cap = new ChromeOptions();
        //禁用w3c
        cap.setExperimentalOption("w3c", false);
        //禁用沙盒
        //cap.addArguments("no-sandbox");
        //无界面启动
        //cap.addArguments("headless");
        LoggingPreferences logP = new LoggingPreferences();
        logP.enable(LogType.PERFORMANCE, Level.ALL);
        cap.setCapability(CapabilityType.LOGGING_PREFS, logP);
        return new ChromeDriver(cap);
    }


    public Map<String, String> loginAndGetHeaders() throws Exception {
        Map<String, String> header = new HashMap<>();
        ChromeDriver chromeDriver = this.getChromeDriver();
        chromeDriver.get("https://www.fednetbank.com/corp/AuthenticationController?__START_TRAN_FLAG__=Y&FORMSGROUP_ID__=AuthenticationFG&__EVENT_ID__=LOAD&FG_BUTTONS__=LOAD&ACTION.LOAD=Y&AuthenticationFG.LOGIN_FLAG=1&BANK_ID=049&LANGUAGE_ID=001");
        chromeDriver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
        WebElement userIdInput = new WebDriverWait(chromeDriver, Duration.ofSeconds(50)).until(ExpectedConditions
                .presenceOfElementLocated(By.name("AuthenticationFG.USER_PRINCIPAL")));
        userIdInput.sendKeys("userId");
        WebElement pwdInput = new WebDriverWait(chromeDriver, Duration.ofSeconds(50)).until(ExpectedConditions
                .presenceOfElementLocated(By.name("AuthenticationFG.ACCESS_CODE")));
        pwdInput.sendKeys("password");
        //获取验证码
        WebElement imageCaptcha = chromeDriver.findElement(By.id("IMAGECAPTCHA"));
        String src = imageCaptcha.getAttribute("src");
        getCatpta(src, chromeDriver);

//        File screenshot = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
//        BufferedImage fullImg = ImageIO.read(screenshot);
//
//        // Get the location of element on the page
//        Point point = imageCaptcha.getLocation();
//
//        // Get width and height of the element
//        int eleWidth = imageCaptcha.getSize().getWidth();
//        int eleHeight = imageCaptcha.getSize().getHeight();
//
//        // Crop the entire page screenshot to get only element screenshot
//        BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(),
//                eleWidth, eleHeight);
//        ImageIO.write(eleScreenshot, "jpg", screenshot);
//        File screenshotLocation = new File("C:\\Users\\Administrator\\Desktop\\captcha.jpg");
//        FileUtils.copyFile(screenshot, screenshotLocation);


        String captcha = "";//this.getImgContent(src);
        WebElement captchaInput = chromeDriver.findElement(By.name("AuthenticationFG.VERIFICATION_CODE"));
        //获取登录按钮
        captchaInput.sendKeys(captcha);
        WebElement loginButton = chromeDriver.findElement(By.name("Action.VALIDATE_CREDENTIALS"));
        loginButton.submit();
        return header;
    }


    public String getCatpta(String url, ChromeDriver chromeDriver) throws Exception {
        Set<Cookie> cookies = chromeDriver.manage().getCookies();

        StringBuffer tmpcookies = new StringBuffer();
        for (Cookie c : cookies) {
            tmpcookies.append(c.toString() + ";");
        }
        Map<String, String> header = new HashMap<>();
        header.put("cookie", tmpcookies.toString());
        Response response = HttpUtil.sent("POST", url, "application/json;charset=UTF-8", "{}", header);
        InputStream inputStream = response.body().byteStream();
        File targetFile = new File("C:\\Users\\Administrator\\Desktop\\captcha.jpg");
        OutputStream outStream = new FileOutputStream(targetFile);

        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        //表示从InputStream中读取度一个数知组的数道据，如果返回-1 则表示数据读版取完成了
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            //生成文件
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.close(); // 关闭输出流
        ITesseract instance = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        instance.setLanguage("eng");//英文库识别数字比较准确

        instance.setDatapath(tessDataFolder.getAbsolutePath());
        //File imageFile = new File("C:\\Users\\Administrator\\Desktop\\captcha1\\captcha.jpg");
//        ITesseract iTesseract = new Tesseract();
        ImageIO.scanForPlugins();
        //BufferedImage image = ImageIO.read(imageFile);
        //String str = instance.doOCR(image).replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", "");
        String str = instance.doOCR(targetFile).replaceAll("[^a-z^A-Z^0-9]", "");
        System.out.println("doOcr======================" + this.doOcr("C:\\Users\\Administrator\\Desktop\\captcha\\captcha.jpg"));
        System.out.println("************************************str=" + str);
        System.out.println("*****************************tens = " + this.tens(ImageIO.read(targetFile)));
        String result = instance.doOCR(targetFile);
        //result = result.replaceAll("[0-9]", "");
//        URL path = ClassLoader.getSystemResource("tessdata");//获得Tesseract的文字库
//        String tesspath = path.getPath().substring(1);zx
//        Tesseract tesseract = new Tesseract();
//        tesseract.setDatapath(tesspath);

        System.out.println("result==============================" + result);
        //String text = iTesseract.doOCR(new File("C:\\Users\\Administrator\\Desktop\\captcha.jpg"));
        test();
        return result;

    }

    public void test() throws Exception {
        CopyOfCleanLines.cleanLinesInImage(new File("C:\\Users\\Administrator\\Desktop\\captcha.jpg"), "C:\\Users\\Administrator\\Desktop\\captcha1");
        ITesseract instance = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        instance.setLanguage("eng");//英文库识别数字比较准确

        instance.setDatapath(tessDataFolder.getAbsolutePath());
        File imageFile = new File("C:\\Users\\Administrator\\Desktop\\captcha1\\captcha.jpg");
//        ITesseract iTesseract = new Tesseract();
        ImageIO.scanForPlugins();
        BufferedImage image = ImageIO.read(imageFile);
        String str = instance.doOCR(image).replaceAll("[^a-z^A-Z^0-9]", "");
        System.out.println("处理后===================" + str.substring(0, 5));
    }


    /**
     * 利用tesseract识别验证码
     */
    public String tens(BufferedImage imagetmp) {
        String result = "";
        try {

            //写入本地生成图片，测试效果用
            //ImageIO.write(imagetmp, "png", new File("D:\\last.png"));

            int width = imagetmp.getWidth();
            int height = imagetmp.getHeight();
            //二值化
            BufferedImage grayImage = new BufferedImage(imagetmp.getWidth(), imagetmp.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = imagetmp.getRGB(i, j);
                    grayImage.setRGB(i, j, rgb);
                }
            }

            //去除干扰线条
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    boolean flag = false;
                    if (isBlack(grayImage.getRGB(x, y))) {
                        //左右均为空时，去掉此点
                        if (isWhite(grayImage.getRGB(x - 1, y)) && isWhite(grayImage.getRGB(x + 1, y))) {
                            flag = true;
                        }
                        //上下均为空时，去掉此点
                        if (isWhite(grayImage.getRGB(x, y + 1)) && isWhite(grayImage.getRGB(x, y - 1))) {
                            flag = true;
                        }
                        //斜上下为空时，去掉此点
                        if (isWhite(grayImage.getRGB(x - 1, y + 1)) && isWhite(grayImage.getRGB(x + 1, y - 1))) {
                            flag = true;
                        }
                        if (isWhite(grayImage.getRGB(x + 1, y + 1)) && isWhite(grayImage.getRGB(x - 1, y - 1))) {
                            flag = true;
                        }
                        if (flag) {
                            grayImage.setRGB(x, y, -1);
                        }
                    }
                }
            }

            //ImageIO.write(grayImage, "png", new File("D:\\after.png"));
            File tessDataFolder = LoadLibs.extractTessResources("tessdata");
            //识别验证码
            ITesseract instance = new Tesseract();
            instance.setDatapath(tessDataFolder.getAbsolutePath());
            instance.setLanguage("eng");
            result = instance.doOCR(grayImage);
        } catch (Exception e) {
            log.error("验证码识别异常..", e);
        }
        return result.replace(" ", "").replace("\n", "").substring(0, 5);
    }

    public boolean isBlack(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 300) {
            return true;
        }
        return false;
    }

    public boolean isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 300) {
            return true;
        }
        return false;
    }


    public String doOcr(String filePathName) throws Exception {
        File file = new File(filePathName);
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("D:\\xsx-tools\\ocr\\tessdata");
        tesseract.setLanguage("eng");
        String result = tesseract.doOCR(file);
        System.out.println(result);
        return result;
    }
}
