package com.xsx.jsoup.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.xsx.jsoup.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

import static org.apache.commons.lang3.BooleanUtils.YES;

/**
 * @Author:夏世雄
 * @Date: 2022/10/11/10:33
 * @Version: 1.0
 * @Discription:
 **/
@Service
@Slf4j
public class ThriedApi {


    private static final String YES = "yes";
    private static final String NO = "no";
    private static final String NOT_FOUND = "notFound";

    public void newBlueLight(JSONObject requestBody) {
        String url = "http://merchant.ezcashwallet.com/api/api/india/policy";
        String jsonString = requestBody.toJSONString();

        Date now = new Date();
        String time = DateUtil.formatDate(now, "yyyyMMddHHmmss");
        try {
            HttpResponse<String> response = Unirest.post(url)
                    .header("alias", "fastrupee")
                    .header("time", time)
                    .header("sign", MD5Util.getMD5Lower("fastrupee" + "fastrupee123" + time))
                    .header("Content-Type", "application/json")
                    .body(Encrypt(jsonString, "03zeGKeK1vDLjStp")).asString();
            String responseBody = response.getBody();

            JSONObject parseObject = JSON.parseObject(responseBody);
            String code = parseObject.getString("code");

        } catch (Exception e) {
            //log.error("error={}", e.getMessage());
        }
    }


    private static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return new org.apache.commons.codec.binary.Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }


    public void riskZeroDegree(JSONObject requestBody) throws Exception {

        //LOGGER.info("零度风控{}", riskOrder.getRiskOrderNo());
        String jsonString = requestBody.toJSONString();
        String merchantNo = "10011";
        String pwd = "741cf57fe465c3c45cabb90181940ae9";
        Long timestamp = System.currentTimeMillis() / 1000L;
        String signParams = "biz_data=" + jsonString + "&merchant_no=" + merchantNo +
                "&pwd=" + pwd + "&timestamp=" + timestamp;
        System.out.println(signParams);
        try {
            String sign = MD5Util.encryptMd5Lower32(signParams);
            SslUtil.ignoreSsl();
            String url = "http://risk.hengyaruiyi.com/v2/india/indiaapi";
            HttpResponse<String> resp = Unirest.post(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("merchant_no", merchantNo)
                    .field("sign", sign)
                    .field("timestamp", timestamp)
                    .field("biz_data", jsonString)
                    .asString();
            String responseBody = resp.getBody();
            System.out.println(responseBody);

//            LOGGER.info("风控单{}零度风控结果为：{}", riskOrder.getRiskOrderNo(), responseBody);
//            RiskZeroDegreeRecord zeroDegreeRecord = new RiskZeroDegreeRecord();
//            zeroDegreeRecord.setCreateTime(new Date());
//            zeroDegreeRecord.setLoanOrderNo(riskOrder.getLoanOrderNo());
//            zeroDegreeRecord.setReqJson(jsonString);
//            zeroDegreeRecord.setRespJson(responseBody);
//            zeroDegreeRecord.setRiskOrderNo(riskOrder.getRiskOrderNo());
//            zeroDegreeRecord.setSpace(riskOrder.getSpace());
//            zeroDegreeRecordService.save(zeroDegreeRecord);
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            int code = jsonObject.getInteger("code");
            String message = jsonObject.getString("message");
//            if (code != 0 && !"NoError".equals(message)) {
//                DingDingUtils.sendNotice(riskOrder.getRiskOrderNo() + " zero degree risk request err " + responseBody, "https://oapi.dingtalk.com/robot/send?access_token=c983eec40ec28bec9d39225602119dd133363c7647ca3c606a9ce440b19ca3d8");
//            }
        } catch (Exception e) {
            System.out.println("error=" + e.getMessage());
            //log.error("error={}", e.getMessage());
//            DingDingUtils.sendExceptionMsgToDingding(e, "zero degree risk error" + riskOrder.getRiskOrderNo(), "error", 0, "https://oapi.dingtalk.com/robot/send?access_token=c983eec40ec28bec9d39225602119dd133363c7647ca3c606a9ce440b19ca3d8");
        }


    }


    public String checkWhatsApp(String mobile) {
        //log.info("根据手机号检查是否安装whatsApp，请求参数mobile={}", mobile);
        int retries = 3;
        String result = NOT_FOUND;
        while (retries > 0 && NOT_FOUND.equals(result)) {
            result = this.doCheckWhatsApp(mobile);
            retries--;
        }
        return result;
    }

    public String doCheckWhatsApp(String mobile) {
        String url = "http://43.204.150.215:9999/api/check";
        try {
            //connectionTimeout  如果设置为0则是用不超时  一般来说连接超时2s差不多，这个可以根据网络情况来定;
            //socketTimeout 如果设置为0则是用不超时  一般设置为30000毫秒，可根据实际情况确定
            //比如有些接口一次处理的数据量比较大，设置成30s 甚至60s的也是有的。
            long start = System.currentTimeMillis();
            Unirest.setTimeouts(5000L, 30000L);
            HttpResponse<String> response = Unirest.post(url)
                    .header("token", "F2AxJxLDVyhTKZSnrT0tlQmzUfrr1nPX")
                    .field("phone", mobile)
                    .asString();
            String body = response.getBody();
            if (response.getStatus() != 200) {
                return NOT_FOUND;
            }
            long end = System.currentTimeMillis();
            System.out.println("执行耗时：" + (end - start) / 1000);
            if (StringUtils.isNotBlank(body) && "TRUE".equalsIgnoreCase(body)) {
                return YES;
            }
            if (StringUtils.isNotBlank(body) && "fAlSe".equalsIgnoreCase(body)) {
                return NO;
            }
            return NOT_FOUND;
        } catch (UnirestException e) {
            System.out.println(e);
            // log.error("根据手机号检查是否安装whatsApp异常，error={}:", e.getMessage());
            return NOT_FOUND;
        }

    }

}



