package com.xsx.jsoup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsx.jsoup.entity.BankUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * @Author:夏世雄
 * @Date: 2022/07/13/14:01
 * @Version: 1.0
 * @E-mail: xiashixiongtoxx@163.com
 * @Discription:
 **/
@Slf4j
public abstract class BaseTest {

    @Autowired
    protected ObjectMapper objectMapper;

//    public ObjectMapper objectMapper() {
//        return new ObjectMapper()
//                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
//                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
//                .registerModule(new JavaTimeModule())
//                ;
//    }


    protected void println(Collection<?> collection) {
        collection.forEach(this::printJson);
    }

    protected void printJson(Object obj) {
        //ObjectMapper objectMapper = objectMapper();
        try {
            System.out.println(objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    protected void debug(Object obj) {
        log.debug("\n{}", obj);
    }

    protected void debug(String msg, Object... obj) {
        log.debug("\n" + msg, obj);
    }


    protected BankUser initBankUser(){
        BankUser bankUser = new BankUser();
        bankUser.setLoginName("9059860438");
        bankUser.setPassword("Wp123456@!");
        bankUser.setBankName("paytm");
        return bankUser;
    }
}

