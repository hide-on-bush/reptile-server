package com.xsx.jsoup.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.xsx.jsoup.entity.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author:夏世雄
 * @Date: 2022/07/25/17:19
 * @Version: 1.0
 * @Discription:
 **/
@Service
public class SendMsg {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String s) {
            System.out.println("CorrelationData: "+correlationData);
            if (ack){
                //如果confirm返回成功 则进行更新
                System.out.println("更新");
            }else{
                //失败则进行具体的后续操作; 重试或者补偿等手段
                System.out.println("异常处理....");
            }
        }
    };

    public void sendOrder(Order order) throws Exception{
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getMessageId()+"");
        rabbitTemplate.setConfirmCallback(confirmCallback);
        String orderStr = JSONObject.toJSONString(order);
        //rabbitTemplate.send("order-exchange","order.demo",new Message(orderStr.getBytes(),new MessageProperties()));
        rabbitTemplate.convertAndSend("order-exchange",   //exchang 交换机
                "order.demo",   //routingKey  路由键
                orderStr,      //消息体内容
                correlationData);       //correlationData 消息唯一ID

    }



}
