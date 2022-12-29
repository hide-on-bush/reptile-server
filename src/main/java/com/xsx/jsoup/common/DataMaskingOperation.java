package com.xsx.jsoup.common;

/**
 * @Author:夏世雄
 * @Date: 2022/10/21/14:50
 * @Version: 1.0
 * @Discription:
 **/
public interface DataMaskingOperation {

    String MASK_CHAR = "*";

    String mask(String content, String maskChar);

}