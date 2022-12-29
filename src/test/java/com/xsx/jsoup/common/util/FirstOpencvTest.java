package com.xsx.jsoup.common.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/12/05/11:10
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class FirstOpencvTest {

    @Test
    void processImage() {
        FirstOpencv.processImage("C:\\Users\\Administrator\\Desktop\\xx4.jpg");
    }

    @Test
    void recognizeText() throws Exception {
        String text = FirstOpencv.recognizeText(new File("C:\\Users\\Administrator\\Desktop\\destImage.jpg"));
        System.out.println(text);
    }
}