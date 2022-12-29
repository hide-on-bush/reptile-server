package com.xsx.jsoup.common.util;

/**
 * @Author:夏世雄
 * @Date: 2022/12/01/10:06
 * @Version: 1.0
 * @Discription:
 **/
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 去除source中的特殊字符
     *
     * @param source
     * @return
     */
    public static String removeSpecialCharacters(String source) {
        String regEx = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*()——+|{}‘；：”“’。， 、？]";
        return source.replaceAll(regEx, "");
    }
}
