package com.xsx.jsoup.service;

import com.xsx.jsoup.common.util.OpencvUtils;
import com.xsx.jsoup.common.util.StringUtils;
import net.sourceforge.tess4j.Tesseract;
import org.junit.jupiter.api.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.net.URL;

import static org.opencv.core.Core.bitwise_not;
import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.*;

/**
 * @Author:夏世雄
 * @Date: 2022/11/28/15:24
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
public class OpenCvTest {

    /**
     * 使用openCv将图片灰度化
     *
     * @throws Exception
     */
    @Test
    public void testOpenCv() throws Exception {
        // 解决awt报错问题
        System.setProperty("java.awt.headless", "false");
        System.out.println(System.getProperty("java.library.path"));
        // 加载动态库
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java460.dll");
        System.load(url.getPath());
        // 读取图像
        Mat image = imread("C:\\Users\\Administrator\\Desktop\\2.jpeg");
        if (image.empty()) {
            throw new Exception("image is empty");
        }
        imshow("Original Image", image);

        // 创建输出单通道图像
        Mat grayImage = new Mat(image.rows(), image.cols(), CvType.CV_8SC1);
        // 进行图像色彩空间转换
        cvtColor(image, grayImage, COLOR_RGB2GRAY);

        imshow("Processed Image", grayImage);
        imwrite("C:\\Users\\Administrator\\Desktop\\9.jpg", grayImage);
        waitKey();
    }


    //图片处理
    @Test
    void test1() {
        System.setProperty("java.awt.headless", "false");
        System.out.println(System.getProperty("java.library.path"));
        // 加载动态库
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java460.dll");
        System.load(url.getPath());
        String filePath = "C:\\Users\\Administrator\\Desktop\\2.jpeg";
        Mat image = imread("C:\\Users\\Administrator\\Desktop\\c.png"); // 加载图像
        imshow("原图", image);

        //高斯滤波器（GaussianFilter）对图像进行平滑处理。
        //GaussianBlur(image, image, new Size(3, 3), 0);

        //这里可以new一个Mat来接收，也可以直接使用原来的image接收cvtColor(image, image, CV_BGRA2GRAY);
        Mat gray = new Mat();
        cvtColor(image, gray, COLOR_RGB2GRAY);// 灰度化
        imshow("灰度", gray);

        //medianBlur(gray, gray, 3);  //中值滤波

        Mat bin = new Mat();
        //第三参数thresh，要根据自己的实际情况改变大小调试打印看一下。一般取100-200
        threshold(gray, bin, 150, 255, THRESH_BINARY);// 二值化
        imshow("二值化", bin);

        bitwise_not(bin, bin); // 反色，即黑色变白色，白色变黑色
        Mat corrode = new Mat();
        Mat expand = new Mat();
        Mat kelner = getStructuringElement(MORPH_RECT, new Size(3, 3), new Point(-1, -1));
        erode(bin, corrode, kelner); // 腐蚀
        dilate(corrode, expand, kelner); // 膨胀
        bitwise_not(expand, expand); // 反色，既黑色变白色，白色变黑色
        imshow("膨胀", expand);

        bitwise_not(bin, bin); // 反色，即黑色变白色，白色变黑色

        //保存二值化图像
        String imageFile = "C:\\Users\\Administrator\\Desktop\\c1.png";
        imwrite(imageFile, expand);

        waitKey(0);
    }

    @Test
    void read() throws Exception {
        File file = new File("C:\\Users\\Administrator\\Desktop\\c1.png");
        Tesseract instance = new Tesseract();
        //设置 tessdata 文件夹 的本地路径
        instance.setDatapath("D:\\xsx-tools\\ocr\\tessdata");
        //设置需要使用的训练集，不设置则默认为eng
        instance.setLanguage("eng");
        //String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*()——+|{}‘；：”“’。， 、？]";
        String str = instance.doOCR(file);
        str = StringUtils.removeSpecialCharacters(str);
        System.out.println(str);
    }

    @Autowired
    OpencvUtils opencvUtils;

    @Test
    void test() {
        opencvUtils.processImage("C:\\Users\\Administrator\\Desktop\\xx4.jpg", "C:\\Users\\Administrator\\Desktop\\xx5.jpg");
    }

    @Test
    void testRead() {
        opencvUtils.read("C:\\Users\\Administrator\\Desktop\\gray.jpg");
    }

    @Test
    void testGray() {
        opencvUtils.saveImage(opencvUtils.gray(
                "C:\\Users\\Administrator\\Desktop\\card1.jpg"), "C:\\Users\\Administrator\\Desktop\\gray.jpg");
    }

    @Test
    void testTwo() {
        opencvUtils.saveImage(opencvUtils.thresholdImage(
                "C:\\Users\\Administrator\\Desktop\\card1.jpg"), "C:\\Users\\Administrator\\Desktop\\threshold.jpg");
    }

    @Test
    void testDilate() {
        opencvUtils.saveImage(opencvUtils.dilateImage(
                "C:\\Users\\Administrator\\Desktop\\card1.jpg"), "C:\\Users\\Administrator\\Desktop\\dilate.jpg");
    }

    @Test
    void testMerge() {
        opencvUtils.merge();
    }

    @Test
    void run() {
        System.setProperty("java.awt.headless", "false");
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java460.dll");
        System.load(url.getPath());
        System.out.println("\nRunning DetectFaceDemo");
        // Create a face detector from the cascade file in the resources
        // directory.

        CascadeClassifier faceDetector = new CascadeClassifier("D:\\xsx-tools\\opencv\\opencv\\build\\etc\\lbpcascades\\lbpcascade_frontalface.xml");
        Mat image1 = Imgcodecs.imread("C:\\Users\\Administrator\\Desktop\\lena.png");
        Mat image = image1.clone();
        imshow("原图", image1);
        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }
        // Save the visualized detection.
        String filename = "faceDetection.png";
        System.out.println(String.format("Writing %s", filename));
        Imgcodecs.imwrite(filename, image);
        imshow("处理之后的图", image);
        waitKey();

    }

    Mat preprocess() {
        System.setProperty("java.awt.headless", "false");
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java460.dll");
        System.load(url.getPath());
        Mat image = Imgcodecs.imread("C:\\Users\\Administrator\\Desktop\\lena.png");
        imshow("原图", image);
        //1.Sobel算子，x方向求梯度
        Mat sobel = new Mat();
        Sobel(image, sobel, CvType.CV_8UC1, 1, 0, 3);
        imshow("sobel", sobel);
        //2.二值化
        Mat binary = new Mat();
        ;
        threshold(sobel, binary, 0, 255, THRESH_OTSU + THRESH_BINARY);
        imshow("binary", binary);
        //3.膨胀和腐蚀操作核设定
        Mat element1 = getStructuringElement(MORPH_RECT, new Size(30, 9));
        //控制高度设置可以控制上下行的膨胀程度，例如3比4的区分能力更强,但也会造成漏检
        Mat element2 = getStructuringElement(MORPH_RECT, new Size(24, 4));

        //4.膨胀一次，让轮廓突出
        Mat dilate1 = new Mat();
        ;
        dilate(binary, dilate1, element2);
        imshow("dilate1", dilate1);
        //5.腐蚀一次，去掉细节，表格线等。这里去掉的是竖直的线
        Mat erode1 = new Mat();
        ;
        erode(dilate1, erode1, element1);
        imshow("erode1", erode1);
        //6.再次膨胀，让轮廓明显一些
        Mat dilate2 = new Mat();
        ;
        dilate(erode1, dilate2, element2);
        imshow("dilate2", dilate2);
        //7.存储中间图片
        imwrite("binary.jpg", binary);
        imwrite("dilate1.jpg", dilate1);
        imwrite("erode1.jpg", erode1);
        imwrite("dilate2.jpg", dilate2);
        waitKey();
        return dilate2;
    }


}
