package com.xsx.jsoup.service;

import com.xsx.jsoup.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author:夏世雄
 * @Date: 2022/07/25/17:39
 * @Version: 1.0
 * @Discription:
 **/

@SpringBootTest
class SendMsgTest {

    @Autowired
    private SendMsg sendMsg;

    @Autowired
    private Recieve recieve;

    @Test
    void sendOrder() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setName("测试订单1");
        order.setMessageId(1l);
        sendMsg.sendOrder(order);
    }

    @Test
    void testReceive() {
        recieve.receive();
    }
}