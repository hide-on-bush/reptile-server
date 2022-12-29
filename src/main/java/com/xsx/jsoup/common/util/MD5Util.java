package com.xsx.jsoup.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author:夏世雄
 * @Date: 2022/10/11/10:34
 * @Version: 1.0
 * @Discription:
 **/
public class MD5Util {
    private static final Logger log = LoggerFactory.getLogger(MD5Util.class);
    // 小写的字符串
    private static char[] DigitLower = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    // 大写的字符串
    private static char[] DigitUpper = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 默认构造函数
     */
    public MD5Util() {
    }

    /**
     * 加密之后的字符串全为小写
     *
     * @param srcStr
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NullPointerException
     */
    public static String getMD5Lower(String srcStr)
            throws NoSuchAlgorithmException {
        String sign = "lower";
        return processStr(srcStr, sign);
    }

    /**
     * MD5 32位小写加密
     */
    public static String encryptMd5Lower32(String encryptStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            //默认小写，在tostring后加toUpperCase()即为大写加密
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptStr;
    }

    /**
     * 加密之后的字符串全为大写
     *
     * @param srcStr
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NullPointerException
     */
    public static String getMD5Upper(String srcStr) {
        String sign = "upper";
        try {
            return processStr(srcStr, sign);
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
            return "";
        }
    }

    private static String processStr(String srcStr, String sign)
            throws NoSuchAlgorithmException, NullPointerException {
        MessageDigest digest;
        // 定义调用的方法
        String algorithm = "MD5";
        // 结果字符串
        String result = "";
        // 初始化并开始进行计算
        digest = MessageDigest.getInstance(algorithm);
        digest.update(srcStr.getBytes());
        byte[] byteRes = digest.digest();

        // 计算byte数组的长度
        int length = byteRes.length;

        // 将byte数组转换成字符串
        for (int i = 0; i < length; i++) {
            result = result + byteHEX(byteRes[i], sign);
        }

        return result;
    }

    /**
     * 将btye数组转换成字符串
     *
     * @param bt
     * @return
     */
    private static String byteHEX(byte bt, String sign) {

        char[] temp = null;
        if (sign.equalsIgnoreCase("lower")) {
            temp = DigitLower;
        } else if (sign.equalsIgnoreCase("upper")) {
            temp = DigitUpper;
        } else {
            throw new RuntimeException("加密缺少必要的条件");
        }
        char[] ob = new char[2];

        ob[0] = temp[(bt >>> 4) & 0X0F];

        ob[1] = temp[bt & 0X0F];

        return new String(ob);
    }

    public static String getMD5(String content) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(content.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    public static void main(String[] args) {
        String content = getMD5("2224358");
        System.out.println(content);
    }

}

