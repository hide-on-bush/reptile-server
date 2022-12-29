package com.xsx.jsoup.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xsx.jsoup.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:夏世雄
 * @Date: 2022/10/21/15:06
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Value("#{'${zero.degree.risk.rejection-code}'.split(',')}")
    private List<Integer> riskCodes;

    //yml中 zeroRisk：
    //          rejectionCode：3,4
    //@Value("#{'${username.userName}'.split(',')}")
    @Test
    void getAll() {


        String str1 = "{\"status\":false,\"data\":{\"code\":5010,\"des\":\"low keep amount\"}}";
        JSONObject json = JSON.parseObject(str1);
        System.out.println(json.getJSONObject("data").getString("des"));
        String msg = "TRANSACTION WITH REFERENCE ID 963896566 FAILED DURING PROCESSING DUE TO INVALID BENEFICIARY MOBILE NUMBER/MMID/ACCOUNT NUMBER..";
        if (msg.contains("TRANSACTION WITH REFERENCE ID") && msg.contains("FAILED DURING")) {
            msg = msg.substring(msg.indexOf("FAILED DURING"));
        }
        System.out.println(msg);
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(4);
        int a = 4;
        if (list1.contains(a)) {
            System.out.println("hello");
        }
        List<User> all = userMapper.getAll();
        System.out.println(riskCodes);
        all.forEach(x -> {
            System.out.println(x.getName());
        });

    }
}