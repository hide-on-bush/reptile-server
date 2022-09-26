package com.xsx.jsoup.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoadLibs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * @Author:夏世雄
 * @Date: 2022/07/28/20:36
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class FednetBankServiceTest {

    @Autowired
    private FednetBankService fednetBankService;

    @Test
    void loginAndGetHeaders() throws Exception{
       // fednetBankService.getVerificationCode("C:\\Users\\Administrator\\Desktop\\captcha1.jpg");
        fednetBankService.loginAndGetHeaders();
    }

    @Test
    void test1() throws Exception{
        ITesseract instance = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        //instance.setLanguage("chi_sim");//英文库识别数字比较准确
        instance.setDatapath(tessDataFolder.getAbsolutePath());
        File imageFile = new File("C:\\Users\\Administrator\\Desktop\\captcha1\\captcha.jpg");
//        ITesseract iTesseract = new Tesseract();
        ImageIO.scanForPlugins();
        BufferedImage image = ImageIO.read(imageFile);
        String ocr = instance.doOCR(image);
        String str = instance.doOCR(image).replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", "");
        //String str = instance.doOCR(image).replaceAll("[^a-z^A-Z^0-9]", "");
        System.out.println("************************************str=" + str);
    }


    @Test
    public void doOcr() throws Exception {
            File file = new File("C:\\Users\\Administrator\\Desktop\\captcha.jpg");
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("D:\\xsx-tools\\ocr\\tessdata");
            tesseract.setLanguage("eng");
            String result = tesseract.doOCR(file);
            System.out.println(result);

    }

}