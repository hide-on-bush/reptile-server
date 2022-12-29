package com.xsx.jsoup.common.util;

/**
 * @Author:夏世雄
 * @Date: 2022/10/12/13:42
 * @Version: 1.0
 * @Discription:
 **/

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.*;


public class SignUtils {

    private static final Logger log = LoggerFactory.getLogger(SignUtils.class);

    public static String sign(Map<String, String> params, boolean trim) {
        SortedMap sortedMap = getParameterMap(params, trim);
        StringBuilder buf = new StringBuilder((sortedMap.size() + 1) * 10);
        SignUtils.buildPayParams(buf, sortedMap, false);
        String preStr = buf.toString();
        log.info("pre_sign = " + preStr);
        String signReceive = sign(preStr, "", "utf-8");
        log.info("after_sign = " + signReceive);
        return signReceive;

    }


    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset)).toUpperCase();
    }


    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    public static String sign(Map<String, String> params, String key, boolean trim) {
        return sign(params, key, "key", trim);

    }

    /**
     * sign
     *
     * @param params
     * @param key
     * @return
     */
    public static String sign(Map<String, String> params, String key) {
        return sign(params, key, "key", true);

    }

    public static String sign(Map<String, String> params, String key, String keyPrefix) {
        return sign(params, key, keyPrefix, true);

    }

    public static String sign(Map<String, String> params, String key, String keyPrefix, boolean trim) {
        SortedMap sortedMap = getParameterMap(params, trim);
        StringBuilder buf = new StringBuilder((sortedMap.size() + 1) * 10);
        SignUtils.buildPayParams(buf, sortedMap, false);
        String preStr = buf.toString();
        log.info("pre_sign = " + preStr);
        String signReceive = sign(preStr, "&" + keyPrefix + "=" + key, "utf-8");
        log.info("after_sign = " + signReceive);
        return signReceive;

    }

    public static String signNoKeyPrefix(Map<String, String> params, String key) {
        SortedMap sortedMap = getParameterMap(params, true);
        StringBuilder buf = new StringBuilder((sortedMap.size() + 1) * 10);
        SignUtils.buildPayParams(buf, sortedMap, false);
        String preStr = buf.toString();
        log.info("pre_sign = " + preStr);
        String signReceive = sign(preStr, key, "utf-8");
        log.info("after_sign = " + signReceive);
        return signReceive;

    }


    /**
     * @param payParams
     * @return
     * @author
     */
    public static void buildPayParams(StringBuilder sb, Map<String, String> payParams, boolean encoding) {
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            String str = payParams.get(key);
            if (StringUtils.isNotBlank(str) && !str.equals("[]")) {
                sb.append(key).append("=");
                if (encoding) {
                    sb.append(urlEncode(str));
                } else {
                    sb.append(str);
                }
                sb.append("&");
            }
        }
        sb.setLength(sb.length() - 1);
    }

    public static SortedMap getParameterMap(Map properties, boolean trim) {
        // 返回值Map
        SortedMap returnMap = new TreeMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            if (trim) {
                returnMap.put(name, value.trim());
            } else {
                returnMap.put(name, value);
            }
        }
        return returnMap;
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            return str;
        }
    }


}


