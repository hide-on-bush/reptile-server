package com.xsx.jsoup.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import com.xsx.jsoup.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author:夏世雄
 * @Date: 2022/07/25/17:20
 * @Version: 1.0
 * @Discription:
 **/
@Service
@Slf4j
public class Recieve {

    @Autowired
    private RabbitTemplate rabbitTemplate;


//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "order-queue",durable = "true"),
//            exchange = @Exchange(name = "order-exchange", durable = "true", type = "topic"),
//            key = "order.*"
//    ))
//    @RabbitHandler
//    public void onOrderMessage(@Payload Order order,
//                               @Headers Map<String,Object> headers,
//                               Channel channel) throws Exception{
//        //消费者操作
//        System.out.println("---- 收到消息,开始消费 ----");
//        System.out.println("订单ID: "+ order.getId());
//        System.out.println("订单名称: "+ order.getName());
//
//        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
//        //手动必须
//        channel.basicAck(deliveryTag,false);
//
//    }


    public void receive(){
        Message receive = rabbitTemplate.receive("order-queue", 3 * 60 * 1000);
        log.info("consumer 开始消费：");
        System.out.println();
        String str = new String(receive.getBody());
        Order order = JSON.parseObject(str, Order.class);
        System.out.println(order);
    }

}
