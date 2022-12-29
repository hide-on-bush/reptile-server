package com.xsx.jsoup.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author:夏世雄
 * @Date: 2022/10/31/14:41
 * @Version: 1.0
 * @Discription:
 **/
public class HttpClientUtil {

    // 连接超时时间
    private static final int CONNECTION_TIMEOUT = 3000;
    //读取超时时间
    private static final int READ_TIMEOUT = 5000;
    // 参数编码
    private static final String ENCODE_CHARSET = "utf-8";


    public static String postRequest(String reqURL, String... params) {
        StringBuilder resultData = new StringBuilder();
        URL url = null;
        try {

            url = new URL(reqURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConn = null;
        InputStreamReader in = null;
        BufferedReader buffer = null;
        String inputLine = null;
        DataOutputStream out = null;
        if (url != null) {
            try {
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);// 设置输入流采用字节流
                urlConn.setDoOutput(true);// 设置输出流采用字节流
                urlConn.setRequestMethod("POST");
                urlConn.setUseCaches(false); // POST请求不能使用缓存
                urlConn.setInstanceFollowRedirects(true);
                urlConn.setConnectTimeout(CONNECTION_TIMEOUT);// 设置连接超时
                urlConn.setReadTimeout(READ_TIMEOUT); // 设置读取超时
                // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
                urlConn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                urlConn.setRequestProperty("Charset", ENCODE_CHARSET);//
                String param = sendPostParams(params);
                urlConn.setRequestProperty("Content-Length",
                        param.getBytes().length + "");//
                // urlConn.setRequestProperty("Connection", "Keep-Alive");
                // //设置长连接
                urlConn.connect();// 连接服务器发送消息
                if (!"".equals(param)) {
                    out = new DataOutputStream(urlConn.getOutputStream());
                    // 将要上传的内容写入流中
                    out.writeBytes(param);
                    // 刷新、关闭
                    out.flush();
                    out.close();

                }
                in = new InputStreamReader(urlConn.getInputStream(),
                        HttpClientUtil.ENCODE_CHARSET);
                buffer = new BufferedReader(in);
                if (urlConn.getResponseCode() == 200) {
                    while ((inputLine = buffer.readLine()) != null) {
                        resultData.append(inputLine);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (buffer != null) {
                        buffer.close();
                    }

                    if (in != null) {
                        in.close();
                    }

                    if (urlConn != null) {
                        urlConn.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultData.toString();
    }

    /**
     * 发送HTTP_GET请求
     *
     * @param httpUrl 请求地址
     *                发送到远程主机的正文数据[a:1,b:2]
     * @return String
     */
    public static String getRequest(String httpUrl, String... params) {
        StringBuilder resultData = new StringBuilder();
        URL url = null;
        try {

            String paramurl = sendGetParams(httpUrl, params);
            url = new URL(paramurl);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConn = null;
        InputStreamReader in = null;
        BufferedReader buffer = null;
        String inputLine = null;
        if (url != null) {
            try {
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.setConnectTimeout(CONNECTION_TIMEOUT);
                in = new InputStreamReader(urlConn.getInputStream(),
                        HttpClientUtil.ENCODE_CHARSET);
                buffer = new BufferedReader(in);
                if (urlConn.getResponseCode() == 200) {
                    while ((inputLine = buffer.readLine()) != null) {
                        resultData.append(inputLine);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (buffer != null) {
                        buffer.close();
                    }

                    if (in != null) {
                        in.close();
                    }

                    if (urlConn != null) {
                        urlConn.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultData.toString();
    }

    /**
     * Post追加参数
     *
     * @param params 发送到远程主机的正文数据[a:1,b:2]
     * @return
     * @see <code>params</code>
     * 请求地址
     */
    private static String sendPostParams(String... params) {
        StringBuilder sbd = new StringBuilder("");
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                String[] temp = params[i].split(":");
                sbd.append(temp[0]);
                sbd.append("=");
                sbd.append(urlEncode(temp[1]));
                sbd.append("&");

            }
            sbd.setLength(sbd.length() - 1);// 删掉最后一个
        }
        return sbd.toString();
    }

    /**
     * Get追加参数
     *
     * @param reqURL 请求地址
     * @param params 发送到远程主机的正文数据[a:1,b:2]
     * @return
     * @see <code>params</code>
     */
    private static String sendGetParams(String reqURL, String... params) {
        StringBuilder sbd = new StringBuilder(reqURL);
        if (params != null && params.length > 0) {
            if (isexist(reqURL, "?")) {// 存在?
                sbd.append("&");
            } else {
                sbd.append("?");
            }
            for (int i = 0; i < params.length; i++) {
                String[] temp = params[i].split(":");
                sbd.append(temp[0]);
                sbd.append("=");
                sbd.append(urlEncode(temp[1]));
                sbd.append("&");

            }
            sbd.setLength(sbd.length() - 1);// 删掉最后一个
        }
        return sbd.toString();
    }

    // 查找某个字符串是否存在
    private static boolean isexist(String str, String fstr) {
        return str.indexOf(fstr) == -1 ? false : true;
    }

    /**
     * 编码
     *
     * @param source
     * @return
     */
    private static String urlEncode(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder
                    .encode(source, HttpClientUtil.ENCODE_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param lat 纬度
     * @param lng 经度
     * @return
     * @throws
     * @Title: getCity
     * @Description: TODO(根据经纬度获取地址 调用百度地图)
     * @date 2021年4月12日 下午2:04:21
     */
    public static String getAddress(double lng, double lat) {
        JSONObject obj = getLocationInfo(lng, lat).getJSONObject("result");
        String address = obj.getString("formatted_address");
        return address;
    }

    /**
     * @param lng 经度
     * @param lat 纬度
     * @return
     */
    public static JSONObject getLocationInfo(double lng, double lat) {
        String url = "http://api.map.baidu.com/geocoder/v2/?location=" + lat + ","
                + lng + "&output=json&ak=XsilXlH0tREQPUujz7aGDLsVkWdz1YO9&pois=0";
        JSONObject obj = JSON.parseObject(HttpClientUtil.getRequest(url));
        return obj;
    }

    public static void main(String[] args) {
        try {
            System.out.println(HttpClientUtil.readStr("C:\\Users\\Administrator\\Desktop\\微信图片2.jpg"));
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        //System.out.println(addressIsInIndia(108.28067 , 33.47788));
        //System.out.println(HttpClientUtil.getLocationInfo(108.28067 , 33.47788));
    }

    /**
     * @param lng 经度
     * @param lat 纬度
     * @return 根据经纬度判断当前地点是否位于india true是，false否
     */
    public static boolean addressIsInIndia(double lng, double lat) {
        Assert.notNull(lng, "经度不能为空！");
        Assert.notNull(lat, "纬度不能为空！");
        JSONObject jsonObject = getLocationInfo(lng, lat);
        String country = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("country");
        return country.equalsIgnoreCase("india");

    }

    public static String readStr(String path) throws TesseractException {
        File file = new File(path);
        Tesseract instance = new Tesseract();
        //设置 tessdata 文件夹 的本地路径
        instance.setDatapath("D:\\xsx-tools\\ocr\\tessdata");
        //设置需要使用的训练集，不设置则默认为eng
        instance.setLanguage("eng");
        return instance.doOCR(file);
    }
}
