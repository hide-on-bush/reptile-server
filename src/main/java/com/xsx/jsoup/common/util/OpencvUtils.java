package com.xsx.jsoup.common.util;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;

import static org.opencv.core.Core.bitwise_not;
import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgproc.Imgproc.dilate;

/**
 * @Author:夏世雄
 * @Date: 2022/12/01/10:12
 * @Version: 1.0
 * @Discription:
 **/
@Slf4j
@Component
public class OpencvUtils {

    @Value("${opencv-dll}")
    private String opencvDll;

    @Value("${ocr.source}")
    private String ocrSource;

    @Value("${ocr.language}")
    private String language;

    /**
     * @param sourceImagePath 原文件存放路径
     * @param targetImagePath 处理之后的文件存放路径
     */
    public void processImage(String sourceImagePath, String targetImagePath) {
        System.setProperty("java.awt.headless", "false");
        URL url = ClassLoader.getSystemResource(opencvDll);
        System.load(url.getPath());
        // 加载图像
        Mat image = imread(sourceImagePath);
        imshow("原图", image);

        //高斯滤波器（GaussianFilter）对图像进行平滑处理。
        //GaussianBlur(image, image, new Size(3, 3), 0);

        //这里可以new一个Mat来接收，也可以直接使用原来的image接收cvtColor(image, image, CV_BGRA2GRAY);
        Mat gray = new Mat();
        // 灰度化
        cvtColor(image, gray, COLOR_RGB2GRAY);
        imshow("灰度", gray);

        //medianBlur(gray, gray, 3);  //中值滤波

        Mat bin = new Mat();
        //第三参数thresh，要根据自己的实际情况改变大小调试打印看一下。一般取100-200
        // 二值化
        threshold(gray, bin, 150, 255, THRESH_BINARY);
        imshow("二值化", bin);

        // 反色，即黑色变白色，白色变黑色
        bitwise_not(bin, bin);
        Mat corrode = new Mat();
        Mat expand = new Mat();
        Mat kelner = getStructuringElement(MORPH_RECT, new Size(3, 3), new Point(-1, -1));
        // 腐蚀
        erode(bin, corrode, kelner);
        // 膨胀
        dilate(corrode, expand, kelner);
        // 反色，既黑色变白色，白色变黑色
        bitwise_not(expand, expand);
        imshow("膨胀", expand);

        // 反色，即黑色变白色，白色变黑色
        bitwise_not(bin, bin);

        //保存二值化图像
        String imageFile = targetImagePath;
        imwrite(imageFile, expand);

        waitKey(0);
    }

    public void saveImage(Mat mat, String targetPath) {
        imwrite(targetPath, mat);
    }

    /**
     * 膨胀、腐蚀
     *
     * @param sourceImagePath
     * @return
     */
    public Mat dilateImage(String sourceImagePath) {
        loadEnv();
        Mat bin = this.thresholdImage(sourceImagePath);
        bitwise_not(bin, bin);
        Mat corrode = new Mat();
        Mat expand = new Mat();
        Mat kelner = getStructuringElement(MORPH_RECT, new Size(3, 3), new Point(-1, -1));
        // 腐蚀
        erode(bin, corrode, kelner);
        // 膨胀
        dilate(corrode, expand, kelner);
        // 反色，既黑色变白色，白色变黑色
        bitwise_not(expand, expand);
        imshow("膨胀", expand);
        return expand;
    }


    public void loadEnv() {
        System.setProperty("java.awt.headless", "false");
        URL url = ClassLoader.getSystemResource(opencvDll);
        System.load(url.getPath());
    }

    /**
     * 灰度
     *
     * @param sourceImagePath
     */
    public Mat gray(String sourceImagePath) {
        loadEnv();
        // 加载图像
        Mat image = imread(sourceImagePath);
        imshow("原图", image);
        Mat gray = new Mat();
        // 灰度化
        cvtColor(image, gray, COLOR_RGB2GRAY);
        imshow("灰度", gray);
//        String imageFile = targetImagePath;
//        imwrite(imageFile, gray);
        return gray;
    }

    /**
     * 二值化
     *
     * @param sourceImagePath
     * @return
     */
    public Mat thresholdImage(String sourceImagePath) {
        loadEnv();
        //Mat image = imread(sourceImagePath);
        Mat bin = new Mat();
        //第三参数thresh，要根据自己的实际情况改变大小调试打印看一下。一般取100-200
        // 二值化
        threshold(this.gray(sourceImagePath), bin, 150, 255, THRESH_BINARY);
        imshow("二值化", bin);
        return bin;
    }

    public String read(String sourcePath) {
        File file = new File(sourcePath);
        Tesseract instance = new Tesseract();
        //设置 tessdata 文件夹 的本地路径
        instance.setDatapath(ocrSource);
        //设置需要使用的训练集，不设置则默认为eng
        instance.setLanguage(language);
        //String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*()——+|{}‘；：”“’。， 、？]";
        String str = null;
        try {
            str = instance.doOCR(file);
            str = StringUtils.removeSpecialCharacters(str);
            System.out.println(str);
        } catch (TesseractException e) {
            log.error("ocr识别出现异常，error={}", e.getMessage());
        }
        return str;
    }


    public void merge() {
        loadEnv();
        Mat src1 = Imgcodecs.imread("C:\\Users\\Administrator\\Desktop\\abc.jpeg");
        Mat src2 = Imgcodecs.imread("C:\\Users\\Administrator\\Desktop\\bcd.jpeg");
        imshow("image1", src1);
        imshow("image2", src2);
        //读取图像到矩阵中
        if (src1.empty() || src2.empty()) {
            throw new RuntimeException("no file");
        }

        Mat dst = new Mat();

        Core.add(src1, src2, dst);

        Imgcodecs.imwrite("C:\\Users\\Administrator\\Desktop\\merge.jpeg", dst);
        imshow("合并之后的图片为", dst);
    }
}
