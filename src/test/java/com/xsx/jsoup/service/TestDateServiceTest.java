package com.xsx.jsoup.service;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.xsx.jsoup.entity.TestDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/10/18/14:08
 * @Version: 1.0
 * @Discription:
 **/
@SpringBootTest
class TestDateServiceTest {

    @Autowired
    private TestDateService testDateService;

    @Test
    void getAll() {
        List<TestDate> testDates = testDateService.getAll();
        testDates.forEach(x -> {
            long createTime = x.getCreateTime().getTime() / 1000L;
            System.out.println(createTime);
        });

        System.out.println(testDates);
    }

//    public Long parsDateToLong(Date date){
//        TimeZone timeZone = TimeZone.getTimeZone("GMT+5:30");
//        TimeZone.setDefault(timeZone);
//        Calendar c = Calendar.getInstance(timeZone);
//        c.setTime(date);
//        return c.getTime().getTime() /1000L;
//    }


    @Test
    void test2() {
        System.out.println(System.currentTimeMillis());
    }
}