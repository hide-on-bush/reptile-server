package com.xsx.jsoup.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/10/11/10:37
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class ThriedApiTest {

    @Autowired
    private ThriedApi thriedApi;


    @Test
    void testZero() throws Exception {
        String json = "";
        thriedApi.riskZeroDegree(JSON.parseObject(json));
    }


    @Test
    void rondom() {
        String str = "{\"data\": \"{\\\"score\\\":\\\"501\\\",\\\"credit_amount\\\":\\\"3500\\\"}\", \"type\": \"IndiaFirstLoan\", \"status\": true, \"aliasId\": \"FEK2022101112434133339cf6\", \"orderId\": \"20221011163014_fastrupee_f14c0dbe8fa740bababe4659cbb4da2d\"}";
        str = str.replace("\\", "");
        System.out.println(str);
        Random random = new Random();
        System.out.println(random.nextInt(10));
    }

    @Test
    void test1() {
        String jsonStr = "{\"merchant_no\":\"10011\",\"biz_data\":\"{\\\"order_no\\\":\\\"JKD202201141025202222xant\\\",\\\"risk_result\\\":4}\",\"sign\":\"4f139de852e23b1252dc206778be72a1\",\"timestamp\":1665726965}";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        String toString = JSON.toJSONString(jsonObject, SerializerFeature.DisableCircularReferenceDetect);

//        System.out.println(jsonString);
//        String toString = JSON.parse(jsonStr).toString();
        System.out.println(toString);
        if (toString.contains("\\")) {
            toString = toString.replace("\\", "");
            if (toString.contains("\"{")) {
                toString = toString.replace("\"{", "{");
            }
            if (toString.contains("}\"")) {
                toString = toString.replace("}\"", "}");
            }
        }
        System.out.println(toString);

        String body = "{\"data\": \"{\\\"amount\\\":\\\"0\\\",\\\"level\\\":\\\"D\\\"}\", \"type\": \"IndiaFirstLoan\", \"status\": true, \"aliasId\": \"FEK202210111441124444pcwc\", \"orderId\": \"20221013150605_fastrupeeapi_2d013789fa404089966a8b20f500368d\"}";
        JSONObject json = JSON.parseObject(body);
        String callBackJson = json.toJSONString();
        if (callBackJson.contains("\\")) {
            callBackJson = callBackJson.replace("\\", "");
            if (callBackJson.contains("\"{")) {
                callBackJson = callBackJson.replace("\"{", "{");
            }
            if (callBackJson.contains("}\"")) {
                callBackJson = callBackJson.replace("}\"", "}");
            }
        }
        System.out.println(callBackJson);
    }

    @Test
    void testCheck() {
        System.out.println(thriedApi.checkWhatsApp("7477793770"));
    }

}