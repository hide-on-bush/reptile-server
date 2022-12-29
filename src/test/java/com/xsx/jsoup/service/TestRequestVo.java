package com.xsx.jsoup.service;

import com.alibaba.fastjson.JSON;
import com.xsx.jsoup.vo.IndiaRequestVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author:夏世雄
 * @Date: 2022/10/08/14:16
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
public class TestRequestVo {
    private String json = "{\n" +
            "    \"aliasId\": \"2222222\",\n" +
            "    \"extend\": \"\",\n" +
            "    \"callUrl\": \"http://192.168.0.148:8080/call/policy\",\n" +
            "    \"phone\": \"123321123\",\n" +
            "    \"serialNumber\": \"21332432434\",\n" +
            "    \"googleId\":\"54545445432\",\n" +
            "    \"channel\": \"2\",\n" +
            "    \"packageName\": \"\",\n" +
            "    \"appName\": \"api\",\n" +
            "    \"loanRate\": \"0.04\",\n" +
            "    \"ztdata\": {\n" +
            "        \"aadhaar\": \"123456\",\n" +
            "        \"pan\": \"888888888\",\n" +
            "        \"imeiIsSame\": 1,\n" +
            "        \"brandModel\": \"vivo_1818\",\n" +
            "        \"age\": 20,\n" +
            "        \"sex\": 0,\n" +
            "        \"applyTime\": \"2020/1/01 13:00:00\",\n" +
            "        \"salaryRange\": \"5\",\n" +
            "        \"marryState\": \"1\",\n" +
            "        \"jobPosition\": \"1\"\n" +
            "    },\n" +
            "    \"msgs\": {\n" +
            "        \"dataList\": [{\n" +
            "            \"content\": \"<MoreRupee>Your saction letter verification code is: 647543. DT Solutions.\",\n" +
            "            \"time\": \"2022/4/30 03:41:50\",\n" +
            "            \"name\": \"BH-DtMORU\",\n" +
            "            \"type\": \"2\"\n" +
            "        },{\n" +
            "            \"content\": \"<VolcanoLoan>Verification code: 043454 -LoStar\",\n" +
            "            \"time\": \"2022/4/30 03:34:23\",\n" +
            "            \"name\": \"VK-LoStar\",\n" +
            "            \"type\": \"2\"\n" +
            "        }],\n" +
            "        \"otherData\": \"\"\n" +
            "    },\n" +
            "    \"txls\": {\n" +
            "        \"dataList\": [{\n" +
            "            \"phoneNumber\": \"9836384542\",\n" +
            "            \"updateTime\": \"2022/4/30 00:10:49\",\n" +
            "            \"createTime\": \"2022/4/30 00:10:49\",\n" +
            "            \"contacts\": \"AnilKumarVerma\",\n" +
            "            \"source\": \"device\"\n" +
            "        },{\n" +
            "            \"phoneNumber\": \"9816351461\",\n" +
            "            \"updateTime\": \"2022/4/30 00:10:49\",\n" +
            "            \"createTime\": \"2022/4/30 00:10:49\",\n" +
            "            \"contacts\": \"Amit0Sharma\",\n" +
            "            \"source\": \"device\"\n" +
            "        }],\n" +
            "        \"otherData\": \"\"\n" +
            "    },\n" +
            "    \"albs\": {\n" +
            "        \"dataList\": [{\n" +
            "            \"name\": \"IMG20210618214916.jpg\",\n" +
            "            \"author\": \"OPPO A31\",\n" +
            "            \"height\": \"4000\",\n" +
            "            \"width\": \"1800\",\n" +
            "            \"longitude\": \"31.1638\",\n" +
            "            \"latitude\": \"77.013\",\n" +
            "            \"date\": \"2021/6/19 03:19:16\",\n" +
            "            \"createTime\": \"2022/4/30 08:06:55\",\n" +
            "            \"model\": \"OPPO A31\"\n" +
            "        },{\n" +
            "            \"name\": \"IMG20210618214928.jpg\",\n" +
            "            \"author\": \"OPPO A31\",\n" +
            "            \"height\": \"4000\",\n" +
            "            \"width\": \"1800\",\n" +
            "            \"longitude\": \"31.1638\",\n" +
            "            \"latitude\": \"77.013\",\n" +
            "            \"date\": \"2021/6/19 03:19:16\",\n" +
            "            \"createTime\": \"2022/4/30 08:06:55\",\n" +
            "            \"model\": \"OPPO A31\"\n" +
            "        }],\n" +
            "        \"otherData\": \"\"\n" +
            "    },\n" +
            "    \"apps\": {\n" +
            "        \"dataList\": [{\n" +
            "            \"installTime\": \"2022/4/20 15:09:36\",\n" +
            "            \"name\": \"Fi\"\n" +
            "        }, {\n" +
            "            \"installTime\": \"2020/8/08 09:49:25\",\n" +
            "            \"name\": \"WPS Office\"\n" +
            "        }, {\n" +
            "            \"installTime\": \"2022/3/10 02:45:30\",\n" +
            "            \"name\": \"Snapchat\"\n" +
            "        }],\n" +
            "        \"otherData\": \"\"\n" +
            "    }\n" +
            "\n" +
            "}";

    @Test
    void print() {
        System.out.println("json转实体类===================");
        IndiaRequestVo indiaRequestVo = JSON.parseObject(json, IndiaRequestVo.class);
        System.out.println(indiaRequestVo);
        System.out.println("实体类转json====================");
        System.out.println(JSON.toJSONString(indiaRequestVo));
    }
}
