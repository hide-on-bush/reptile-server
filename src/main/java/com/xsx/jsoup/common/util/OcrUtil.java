package com.xsx.jsoup.common.util;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoadLibs;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @Author:夏世雄
 * @Date: 2022/12/01/12:01
 * @Version: 1.0
 * @Discription:
 **/
public class OcrUtil {

    /**
     * 识别图片信息
     *
     * @param img
     * @return
     */
    public static String getImageMessage(BufferedImage img, String language) {
        String result = "end";
        try {
//            ITesseract instance = new Tesseract();
//            File tessDataFolder = LoadLibs.extractTessResources("tessdata");
//            instance.setLanguage(language);
//            instance.setDatapath(tessDataFolder.getAbsolutePath());
            Tesseract instance = new Tesseract();
            //设置 tessdata 文件夹 的本地路径
            //设置需要使用的训练集，不设置则默认为eng
            instance.setDatapath("D:\\xsx-tools\\ocr\\tessdata");
            instance.setLanguage(language);
            result = instance.doOCR(img);
            //System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

}
